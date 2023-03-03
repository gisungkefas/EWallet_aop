package com.kefas.EWallet_aop.pojo.paystack.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerValidationResponse {
    private boolean status;
    private String message;
}
