package com.sunny.scm.identity.mapper;


import com.sunny.scm.identity.dto.response.CompanyResponse;
import com.sunny.scm.identity.entity.Company;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
    CompanyResponse toDto(Company company);
    //Company toEntity();
}
