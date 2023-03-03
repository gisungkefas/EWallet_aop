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
public class RegisterRequest {
    @NotBlank(message = "firstName must be balance")
    private String firstName;
    @NotBlank(message = "lastName must be balance")
    private String lastName;
    @Email
    @NotBlank(message = "email must be balance")
    private String email;
    @NotBlank(message = "password must be balance")
    private String password;
    @NotBlank(message = "country must be balance")
    private String country;
    @NotBlank(message = "state must be balance")
    private String state;
    @NotBlank(message = "homeAddress must be balance")
    private String homeAddress;
    @NotBlank(message = "phoneNumber must be balance")
    private String phoneNumber;
}
