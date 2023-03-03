package com.kefas.EWallet_aop.pojo.paystack.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetUpTransactionRequest {
    @NotBlank(message = "email is mandatory")
    @Schema(example = "example@abc.com")
    private String email;
    @Schema(example = "Amount should be in Kobo, Pesewas or Cents")
    @NotBlank(message = "amount should be specified in Kobo, pesewas or cents")
    private String amount;
    @Schema(example = "Currency NGN")
    private String currency;
}
