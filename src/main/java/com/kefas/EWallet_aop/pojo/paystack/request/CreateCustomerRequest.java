package com.kefas.EWallet_aop.pojo.paystack.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class CreateCustomerRequest {
    @Email
    @NotBlank(message = "Email is mandatory")
    @Schema(example = "example@abc.com")
    private String email;
    @Schema(example = "Mark")
    @NotBlank(message = "First name is mandatory")
    private String first_name;
    @Schema(example = "Di")
    @NotBlank(message = "Last name is mandatory")
    private String last_name;
    @NotBlank(message = "Phone number is mandatory")
    @Schema(example = "08000000000")
    private String phone;
}
