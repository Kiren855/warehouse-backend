package com.sunny.scm.identity.service.impl;


import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.identity.client.KeycloakClient;
import com.sunny.scm.identity.constant.GrantType;
import com.sunny.scm.identity.dto.param.CredentialParam;
import com.sunny.scm.identity.dto.request.LoginRootRequest;
import com.sunny.scm.identity.dto.request.RegisterRootRequest;
import com.sunny.scm.identity.dto.request.TokenRequest;
import com.sunny.scm.identity.dto.request.UserCreationRequest;
import com.sunny.scm.identity.entity.Company;
import com.sunny.scm.identity.entity.User;
import com.sunny.scm.identity.entity.UserType;
import com.sunny.scm.identity.mapper.CompanyMapper;
import com.sunny.scm.identity.repository.CompanyRepository;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.IdentityRootService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityRootServiceImpl implements IdentityRootService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final KeycloakClient keycloakClient;

    @Value("${keycloak.client-id}")
    String clientId;

    @Value("${keycloak.client-secret}")
    String clientSecret;
    @Override
    @Transactional
    public String register(RegisterRootRequest request) {
        Company company = companyMapper.toEntity(request);
        companyRepository.save(company);

        try {
            var userCreationRequest = UserCreationRequest.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .emailVerified(true)
                    .enabled(true)
                    .attributes(Map.of("company_id", List.of(company.getId().toString())))
                    .realmRoles(Map.of())
                    .credentials(List.of(CredentialParam.builder()
                            .type("password")
                            .value(request.getPassword())
                            .temporary(false)
                            .build()))
                    .build();

            String clientToken = getClientToken();
            var createUserResponse = keycloakClient.createUser(
                "Bearer " + clientToken,
                userCreationRequest
            );

            String userId = extractUserId(createUserResponse);
            User newUser = User.builder()
                    .userId(userId)
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .userType(UserType.ROOT)
                    .companyId(company.getId())
                    .isActive(true)
                    .parentId(null)
                    .build();

            userRepository.save(newUser);

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

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] splitedStr = location.split("/");
        return splitedStr[splitedStr.length - 1];
    }

    private String getClientToken() {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", GrantType.CLIENT_CREDENTIALS.getValue());
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("scope", "openid");

        var clientToken = keycloakClient.exchangeToken(form);
        return clientToken.getAccessToken();
    }
}
