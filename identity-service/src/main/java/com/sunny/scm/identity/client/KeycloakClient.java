package com.sunny.scm.identity.client;

import com.sunny.scm.identity.dto.param.RoleParam;
import com.sunny.scm.identity.dto.param.TokenExchangeParam;
import com.sunny.scm.identity.dto.request.UserCreationRequest;
import com.sunny.scm.identity.dto.response.TokenExchangeResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;


@FeignClient(name = "identity-client", url = "${keycloak.url}")
public interface KeycloakClient {

    @PostMapping(
            value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenExchangeResponse exchangeToken(
            @QueryMap TokenExchangeParam tokenExchangeParam);

    @PostMapping(
            value = "/admin/realms/${keycloak.realm}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody UserCreationRequest request
    );

    @PostMapping(
            value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> login(
            @RequestHeader("authorization") String token,
            @QueryMap TokenExchangeParam tokenExchangeParam
    );


    @PostMapping(
            value = "/realms/${keycloak.realm/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void logout(
            @RequestHeader("authorization") String token,
            @QueryMap TokenExchangeParam param
    );

    @PostMapping(
            value = "/admin/realms/${keycloak.realm}/users/{userId}/role-mappings/realm",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void assignRealmRole(
            @RequestHeader("authorization") String token,
            @PathVariable("userId") String userId,
            @RequestBody List<RoleParam> roles
    );
} 
