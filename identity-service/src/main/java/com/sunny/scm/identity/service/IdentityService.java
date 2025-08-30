package com.sunny.scm.identity.service;


import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.RegisterSubRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;


public interface IdentityService {
    String register(RegisterRootRequest request);
    String register(RegisterSubRequest request);
    Object login(LoginRootRequest request);
    Object refreshToken(TokenRequest request);
}
