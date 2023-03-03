package com.kefas.EWallet_aop.pojo.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    @Email
    private String email;
    @NotBlank(message = "password must be balance")
    private String password;
    @NotBlank(message = "confirmPassword must be balance")
    private String confirmPassword;
}
