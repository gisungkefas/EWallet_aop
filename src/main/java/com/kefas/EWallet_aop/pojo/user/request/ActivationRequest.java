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
public class ActivationRequest {
    @Email
    private String email;
    @NotBlank(message = "token must be balance")
    private String token;
}
