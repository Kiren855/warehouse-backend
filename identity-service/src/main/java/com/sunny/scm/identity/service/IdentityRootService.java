package com.sunny.scm.identity.service;


import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;
import com.sunny.scm.identity.repository.UserRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public interface IdentityRootService {
    String register(RegisterRootRequest request);
    Object login(LoginRootRequest request);
    Object refreshToken(TokenRequest request);
}
