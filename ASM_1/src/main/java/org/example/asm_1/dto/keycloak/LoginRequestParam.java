package org.example.asm_1.dto.keycloak;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequestParam {
    private String grant_type;
    private String client_id;
    private String client_secret;
    private String username;
    private String password;
    private String scope;
}
