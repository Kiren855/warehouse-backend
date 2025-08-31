package com.sunny.scm.identity.service;


import com.sunny.scm.identity.dto.auth.*;


public interface IdentityService {
    String register(RegisterRootRequest request);
    String register(RegisterSubRequest request);
    TokenExchangeResponse login(LoginRootRequest request);
    TokenExchangeResponse refreshToken(TokenRequest request);
}
