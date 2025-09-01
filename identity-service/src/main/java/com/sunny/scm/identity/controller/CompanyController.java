package com.sunny.scm.identity.controller;


import com.sunny.scm.common.dto.ApiResponse;
import com.sunny.scm.identity.constant.IdentitySuccessCode;
import com.sunny.scm.identity.dto.company.UpdateCompanyRequest;
import com.sunny.scm.identity.service.CompanyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/identity/api/v1/companies")
public class CompanyController {
    private final CompanyService companyService;

    @PutMapping()
    public ResponseEntity<?> updateCompany(@Valid @RequestBody UpdateCompanyRequest request) {
        companyService.updateCompany(request);
        IdentitySuccessCode successCode = IdentitySuccessCode.COMPANY_UPDATE_SUCCESS;
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(successCode.getCode())
                .message(successCode.getMessage())
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
