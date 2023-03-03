package com.kefas.EWallet_aop.pojo.user.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private String email;

    public static LoginResponse mapFromData(String token, String email) {
        return LoginResponse.builder()
                .token(token)
                .email(email)
                .build();
    }
}
