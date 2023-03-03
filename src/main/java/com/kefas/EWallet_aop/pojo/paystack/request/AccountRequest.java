package com.kefas.EWallet_aop.pojo.paystack.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    @Schema(example = "nuban")
    @NotBlank(message = "type is mandatory")
    private String type;
    @Schema(example = "Mark Di")
    @NotBlank(message = "name is mandatory")
    private String name;
    @Schema(example = "0001234567")
    @NotBlank(message = "account_number is mandatory")
    private String account_number;
    @Schema(example = "058 for GTB")
    @NotBlank(message = "bank_code is mandatory")
    private String bank_code;
    @Schema(example = "NGN")
    @NotBlank(message = "currency is mandatory")
    private String currency;
}
