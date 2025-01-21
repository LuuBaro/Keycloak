package org.example.asm_1.repository;

import feign.QueryMap;
import org.example.asm_1.dto.keycloak.LoginRequestParam;
import org.example.asm_1.dto.keycloak.TokenExchangeParam;
import org.example.asm_1.dto.keycloak.TokenExchangeResponse;
import org.example.asm_1.dto.keycloak.UserCreationParam;
import org.example.asm_1.dto.response.LoginResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "identity-client", url = "http://localhost:9090")
public interface KeycloakRepository {
    @PostMapping(value = "/realms/ASM/protocol/openid-connect/token",
            consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    TokenExchangeResponse exchangeToken(@QueryMap TokenExchangeParam param);

    @PostMapping(value = "/admin/realms/ASM/users",
            consumes= MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<?> createUser(
            @RequestHeader("authorization") String token,
            @RequestBody UserCreationParam param);

    @PostMapping(value = "/realms/ASM/protocol/openid-connect/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    LoginResponse exchangeToken(@QueryMap LoginRequestParam param);

}

