package com.sunny.scm.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomPrincipal {
    String userId;
    Long companyId;
}
