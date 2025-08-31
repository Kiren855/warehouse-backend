package com.sunny.scm.identity.service;


import com.sunny.scm.identity.dto.auth.LoginRootRequest;
import com.sunny.scm.identity.dto.auth.RegisterRootRequest;
import com.sunny.scm.identity.dto.auth.RegisterSubRequest;
import com.sunny.scm.identity.dto.auth.TokenRequest;


public interface IdentityService {
    String register(RegisterRootRequest request);
    String register(RegisterSubRequest request);
    Object login(LoginRootRequest request);
    Object refreshToken(TokenRequest request);
}
