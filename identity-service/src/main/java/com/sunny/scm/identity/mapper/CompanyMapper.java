package com.sunny.scm.identity.mapper;


import com.sunny.scm.identity.dto.auth.RegisterRootRequest;
import com.sunny.scm.identity.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(source = "companyName", target = "name")
    @Mapping(source = "legalName", target = "legalName")
    @Mapping(source = "taxId", target = "taxId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "companyPhone", target = "phone")
    Company toEntity(RegisterRootRequest request);
}
