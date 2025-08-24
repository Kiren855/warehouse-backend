package com.sunny.scm.identity.service.impl;


import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;
import com.sunny.scm.identity.service.IdentityRootService;
import org.springframework.stereotype.Service;

@Service
public class IdentityRootServiceImpl implements IdentityRootService {
    @Override
    public String register(RegisterRootRequest request) {
        return null;
    }

    @Override
    public Object login(LoginRootRequest request) {
        return null;
    }

    @Override
    public Object refreshToken(TokenRequest request) {
        return null;
    }
}
