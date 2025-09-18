package com.sunny.scm.identity.service.impl;

import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.identity.constant.IdentityErrorCode;
import com.sunny.scm.identity.dto.company.UpdateCompanyRequest;
import com.sunny.scm.identity.entity.Company;
import com.sunny.scm.identity.repository.CompanyRepository;
import com.sunny.scm.identity.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    @Override
    public void updateCompany(UpdateCompanyRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        Company company = companyRepository.findById(Long.valueOf(companyId))
                .orElseThrow(() -> new AppException(IdentityErrorCode.COMPANY_NOT_EXISTS));

        companyRepository.findByName(request.getCompanyName())
                        .ifPresent( entity -> {
                           throw new AppException(IdentityErrorCode.COMPANY_NAME_ALREADY_EXISTS);
                        });

        companyRepository.findByTaxId(request.getTaxId())
                        .ifPresent( entity -> {
                           throw new AppException(IdentityErrorCode.COMPANY_TAX_ID_ALREADY_EXISTS);
                        });

        company.setName(request.getCompanyName());
        company.setAddress(request.getAddress());
        company.setLegalName(request.getLegalName());
        company.setTaxId(request.getTaxId());
        company.setPhone(request.getCompanyPhone());

        companyRepository.save(company);
    }
}
