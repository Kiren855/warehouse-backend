package com.sunny.scm.warehouse.client;

import java.io.InputStream;

public interface FileStorageClient {
    String uploadFile(String containerName, String originalFileName, InputStream data, long lengh);

    void deleteFile(String containerName, String fileName);
}
