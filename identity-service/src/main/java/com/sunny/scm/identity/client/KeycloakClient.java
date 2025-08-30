package com.sunny.scm.identity.client;

import com.sunny.scm.identity.dto.param.RoleParam;
import com.sunny.scm.identity.dto.param.TokenExchangeParam;
import com.sunny.scm.identity.dto.request.UserCreationRequest;
import com.sunny.scm.identity.dto.response.TokenExchangeResponse;
import feign.QueryMap;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@FeignClient(name = "identity-client", url = "${keycloak.url}")
public interface KeycloakClient {
    @PostMapping(
            value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    TokenExchangeResponse exchangeToken(@RequestBody MultiValueMap<String, String> formParams);

    @PostMapping(
            value = "/admin/realms/${keycloak.realm}/users",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody UserCreationRequest request
    );

    @PostMapping(
            value = "/admin/realms/${keycloak.realm}/users/{userId}/role-mappings/realm",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    void assignRole(
            @RequestHeader("authorization") String token,
            @PathVariable String userId,
            @RequestBody List<RoleParam> roles
    );

    @PostMapping(
            value = "/realms/${keycloak.realm}/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    ResponseEntity<?> login(
            @RequestHeader("authorization") String token,
            @RequestBody MultiValueMap<String, String> formParams
    );


    @PostMapping(
            value = "/realms/${keycloak.realm/protocol/openid-connect/logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void logout(
            @RequestHeader("authorization") String token,
            @QueryMap TokenExchangeParam param
    );


}
