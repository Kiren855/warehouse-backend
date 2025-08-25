package com.sunny.scm.identity.service.impl;


import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.identity.client.KeycloakClient;
import com.sunny.scm.identity.constant.GrantType;
import com.sunny.scm.identity.dto.param.TokenExchangeParam;
import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.IdentityRootService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityRootServiceImpl implements IdentityRootService {
    private final UserRepository userRepository;
    private final KeycloakClient keycloakClient;

    @Value("${keycloak.client-id}")
    String clientId;

    @Value("${keycloak.client-secret}")
    String clientSecret;
    @Override
    public String register(RegisterRootRequest request) {
        try {
            var clientToken = keycloakClient.exchangeToken(TokenExchangeParam.builder()
                    .client_id(clientId)
                    .clientSecret(clientSecret)
                    .grantType(GrantType.CLIENT_CREDENTIALS.getValue())
                    .scope("openid")
                    .build());

            log.info("token {}", clientToken);
            return "register successfully";

        } catch(FeignException e) {
            log.error(e.getMessage());
            throw new AppException(GlobalErrorCode.UNCATEGORIZED_EXCEPTION);
        }
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
