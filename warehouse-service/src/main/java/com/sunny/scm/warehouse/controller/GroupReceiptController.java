package com.sunny.scm.warehouse.controller;

import com.sunny.scm.warehouse.constant.WarehouseErrorCode;
import com.sunny.scm.warehouse.service.GroupReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("warehouse/api/v1/group-receipts")
@RequiredArgsConstructor
public class GroupReceiptController {
    private final GroupReceiptService groupReceiptService;

    @GetMapping("/{groupReceiptId}/download")
    public ResponseEntity<byte[]> downloadPutawayList(@PathVariable Long groupReceiptId) {
        try {
            byte[] fileData = groupReceiptService.downloadPutawayList(groupReceiptId);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "putaway_instructions_" + groupReceiptId + ".pdf");
            headers.setContentLength(fileData.length);

            return new ResponseEntity<>(fileData, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            if (e.getMessage().contains(WarehouseErrorCode.GROUP_RECEIPT_NOT_FOUND.toString()) ||
                    e.getMessage().contains(WarehouseErrorCode.PUTAWAY_PDF_NOT_FOUND.toString())) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            throw e;
        }
    }
}
