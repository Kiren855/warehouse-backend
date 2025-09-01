package com.sunny.scm.identity.service.impl;


import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.identity.client.KeycloakClient;
import com.sunny.scm.identity.constant.GrantType;
import com.sunny.scm.identity.constant.IdentityErrorCode;
import com.sunny.scm.identity.constant.RealmRoles;
import com.sunny.scm.identity.dto.auth.*;
import com.sunny.scm.identity.entity.Company;
import com.sunny.scm.identity.entity.User;
import com.sunny.scm.identity.entity.UserType;
import com.sunny.scm.identity.repository.CompanyRepository;
import com.sunny.scm.identity.repository.UserRepository;
import com.sunny.scm.identity.service.IdentityService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityServiceImpl implements IdentityService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final KeycloakClient keycloakClient;

    @Value("${keycloak.client-id}")
    String clientId;

    @Value("${keycloak.client-secret}")
    String clientSecret;
    @Override
    @Transactional
    public String register(RegisterRootRequest request) {
        Company company = new Company();
        companyRepository.save(company);
        try {
            // create payload request
            var userCreationRequest = UserCreationRequest.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .emailVerified(true)
                    .enabled(true)
                    .attributes(Map.of("company_id", List.of(company.getId().toString())))
                    .credentials(List.of(CredentialParam.builder()
                            .type("password")
                            .value(request.getPassword())
                            .temporary(false)
                            .build()))
                    .build();

            // get client token
            String clientToken = getClientToken();

            //call keycloak api to create user
            var createUserResponse = keycloakClient.createUser(
                "Bearer " + clientToken,
                userCreationRequest
            );

            String userId = extractUserId(createUserResponse);
            keycloakClient.assignRole(
                "Bearer " + clientToken,
                userId,
                List.of(RoleParam.builder()
                        .id(RealmRoles.ROOT.getId())
                        .name(RealmRoles.ROOT.getName())
                        .build())
            );

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
    @Transactional
    public String register(RegisterSubRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String userId = authentication.getName();
            String companyId = jwt.getClaimAsString("company_id");

            var userCreationRequest = UserCreationRequest.builder()
                    .username(request.getUsername())
                    .emailVerified(true)
                    .enabled(true)
                    .attributes(Map.of("company_id", List.of(companyId)))
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

            String subUserId = extractUserId(createUserResponse);
            keycloakClient.assignRole(
                    "Bearer " + clientToken,
                    subUserId,
                    List.of(RoleParam.builder()
                            .id(RealmRoles.SUB.getId())
                            .name(RealmRoles.SUB.getName())
                            .build())
            );

            User newUser = User.builder()
                    .userId(subUserId)
                    .username(request.getUsername())
                    .userType(UserType.SUB)
                    .companyId(Long.valueOf(companyId))
                    .isActive(true)
                    .parentId(userId)
                    .build();

            userRepository.save(newUser);

            return "register sub-user successfully";
        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new AppException(GlobalErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public TokenExchangeResponse login(LoginRootRequest request) {
        try {
            String loginId = request.getUsername() != null && !request.getUsername().isBlank()
                    ? request.getUsername()
                    : request.getEmail();

            String clientToken = getClientToken();

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", GrantType.PASSWORD.getValue());
            form.add("client_id", clientId);
            form.add("client_secret", clientSecret);
            form.add("scope", "openid");
            form.add("username", loginId);
            form.add("password", request.getPassword());

            return keycloakClient.login(clientToken, form);

        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new AppException(IdentityErrorCode.ACCOUNT_NOT_EXISTS);
        }
    }

    @Override
    public TokenExchangeResponse refreshToken(String token) {
        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("grant_type", GrantType.REFRESH_TOKEN.getValue());
            form.add("client_id", clientId);
            form.add("client_secret", clientSecret);
            form.add("refresh_token", token);

            return keycloakClient.exchangeToken(form);

        } catch (FeignException e) {
            log.info(e.getMessage());
            throw  new AppException(IdentityErrorCode.REFRESH_TOKEN_ERROR);
        }
    }

    @Override
    public void logout(String token) {
        try {
            String clientToken = getClientToken();

            MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
            form.add("client_id", clientId);
            form.add("client_secret", clientSecret);
            form.add("refresh_token", token);

            keycloakClient.logout(clientToken, form);

        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new AppException(IdentityErrorCode.LOGOUT_ERROR);
        }
    }

    private String extractUserId(ResponseEntity<?> response) {
        String location = response.getHeaders().get("Location").getFirst();
        String[] splitStr = location.split("/");
        return splitStr[splitStr.length - 1];
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
