package com.sunny.scm.warehouse.client;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.azure.storage.blob.models.BlobStorageException;
import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AzureFileStorageClient implements FileStorageClient{
    private final BlobServiceClient blobServiceClient;
    @Override
    public String uploadFile(String containerName, String originalFileName, InputStream data, long lengh) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
            String newFileName = UUID.randomUUID() + originalFileName.substring(originalFileName.lastIndexOf("."));
            String fileExtension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
            String contentType = getContentType(fileExtension);

            BlobClient blobClient = blobContainerClient.getBlobClient(newFileName);
            BlobHttpHeaders blobHttpHeaders = new BlobHttpHeaders()
                    .setContentType(contentType);

            blobClient.upload(data, lengh, true);
            blobClient.setHttpHeaders(blobHttpHeaders);

            return blobClient.getBlobUrl();
        }catch (BlobStorageException e) {
            throw new AppException(GlobalErrorCode.FILE_CANNOT_UPLOAD);
        }
    }

    @Override
    public void deleteFile(String containerName, String fileName) {
        try {
            BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);

            BlobClient blobClient = blobContainerClient.getBlobClient(fileName);

            if (blobClient.exists()) {
                blobClient.delete();
            } else {
                throw new AppException(GlobalErrorCode.FILE_NOT_FOUND);
            }
        } catch (BlobStorageException e) {
            throw new AppException(GlobalErrorCode.FILE_CANNOT_DELETE);
        }
    }

    private String getContentType(String fileExtension) {
        return switch (fileExtension) {
            case "mp4" -> "video/mp4";
            case "jpg", "jpeg" -> "image/jpeg";
            case "png" -> "image/png";
            case "gif" -> "image/gif";
            case "bmp" -> "image/bmp";
            case "pdf" -> "application/pdf";
            case "doc" -> "application/msword";
            case "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            case "m3u8" -> "application/x-mpegURL";
            case "ts" -> "video/mp2t";
            case "m4s" -> "video/iso.segment";
            default -> "application/octet-stream";
        };
    }
}
