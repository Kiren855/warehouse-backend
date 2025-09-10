package com.sunny.scm.identity.service.impl;


import com.sunny.scm.common.constant.GlobalErrorCode;
import com.sunny.scm.common.dto.RoleResponse;
import com.sunny.scm.common.exception.AppException;
import com.sunny.scm.common.service.RedisService;
import com.sunny.scm.identity.client.KeycloakClient;
import com.sunny.scm.identity.constant.*;
import com.sunny.scm.identity.dto.auth.*;
import com.sunny.scm.identity.entity.Company;
import com.sunny.scm.identity.entity.User;
import com.sunny.scm.identity.event.LoggingProducer;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdentityServiceImpl implements IdentityService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final KeycloakClient keycloakClient;
    private final RedisService redisService;
    private final LoggingProducer loggingProducer;

    @Value("${keycloak.client-id}")
    String clientId;

    @Value("${keycloak.client-secret}")
    String clientSecret;
    @Override
    @Transactional
    public String register(RegisterRootRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new AppException(IdentityErrorCode.ACCOUNT_ALREADY_EXISTS);
                });

        Company company = new Company();
        companyRepository.save(company);
        try {
            // create payload request
            var userCreationRequest = UserCreationRequest.builder()
                    .email(request.getEmail())
                    .username(request.getEmail())
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
    public String register(RegisterSubRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String userId = authentication.getName();
            String companyId = jwt.getClaimAsString("company_id");
            String username = request.getUsername();

            userRepository.findByCompanyIdAndUsername(Long.valueOf(companyId), username).ifPresent(user -> {
                throw new AppException(IdentityErrorCode.ACCOUNT_ALREADY_EXISTS);
            });

            var userCreationRequest = UserCreationRequest.builder()
                    .username(username)
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
                    .username(username)
                    .userType(UserType.SUB)
                    .companyId(Long.valueOf(companyId))
                    .isActive(true)
                    .parentId(userId)
                    .build();

            userRepository.save(newUser);
            String activity = LogAction.CREATE_USER.format(request.getUsername());
            loggingProducer.sendMessage(userId, "ROOT",
                    Long.valueOf(companyId), activity);

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

            User user = userRepository.findByUsernameOrEmail(loginId, loginId)
                    .orElseThrow(() -> new AppException(IdentityErrorCode.USERNAME_OR_PASSWORD_UNCORRECT));

            if (!user.isActive()) {
                throw new AppException(IdentityErrorCode.ACCOUNT_DISABLED);
            }

            // store roles into redis
            createUserRoles(user.getUserId());

            // get token client
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
    public void changeSubPassword(String userId, ChangePasswordRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String companyId = jwt.getClaimAsString("company_id");

        try {

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new AppException(IdentityErrorCode.ACCOUNT_NOT_EXISTS));

            String clientToken = getClientToken();

            CredentialParam credential = CredentialParam.builder()
                    .type("password")
                    .value(request.getNewPassword())
                    .temporary(false)
                    .build();

            keycloakClient.resetPassword(
                    "Bearer " + clientToken,
                    user.getUserId(),
                    credential
            );
        String action = LogAction.CHANGE_PASSWORD.format(user.getUsername());
        loggingProducer.sendMessage(userId, "ROOT",
                Long.valueOf(companyId), action);

        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new AppException(GlobalErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    @Override
    public void changeRootPassword(ChangePasswordRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String userId = authentication.getName();

            User user = userRepository.findByUserId(userId)
                    .orElseThrow(() -> new AppException(IdentityErrorCode.ACCOUNT_NOT_EXISTS));

            String clientToken = getClientToken();

            CredentialParam credential = CredentialParam.builder()
                    .type("password")
                    .value(request.getNewPassword())
                    .temporary(false)
                    .build();

            keycloakClient.resetPassword(
                    "Bearer " + clientToken,
                    user.getUserId(),
                    credential
            );

        } catch (FeignException e) {
            log.error(e.getMessage());
            throw new AppException(GlobalErrorCode.UNCATEGORIZED_EXCEPTION);
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
    private void createUserRoles(String userId) {
        List<RoleResponse> roles = userRepository.findRolesByUserId(userId)
                .stream().map(role -> RoleResponse.builder()
                        .id(role.getId())
                        .roleName(role.getRoleName())
                        .build()).collect(Collectors.toList());

        String key = "user_roles_" + userId;
        redisService.setValue(key, roles, 1800);
        log.info("Stored roles for user with key: {} : {}", key, roles);
    }
}
