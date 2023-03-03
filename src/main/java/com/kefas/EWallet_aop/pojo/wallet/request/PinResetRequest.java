package com.kefas.EWallet_aop.pojo.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PinResetRequest {
    @NotBlank(message = "oldPin must be balance")
    private String oldPin;
    @NotBlank(message = "newPin must be balance")
    private String newPin;
    @NotBlank(message = "confirmNewPin must be balance")
    private String confirmNewPin;
}
