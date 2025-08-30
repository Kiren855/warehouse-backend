package com.sunny.scm.identity.service;

import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.LoginSubRequest;
import com.sunny.scm.identity.dto.request.RegisterSubRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;

public interface IdentitySubService {
    String register(RegisterSubRequest request);
    Object login(LoginSubRequest request);
    Object refreshToken(TokenRequest request);
}
