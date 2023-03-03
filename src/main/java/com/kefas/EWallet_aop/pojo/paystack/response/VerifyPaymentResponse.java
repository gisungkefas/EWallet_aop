package com.kefas.EWallet_aop.pojo.paystack.response;

import com.kefas.EWallet_aop.pojo.paystack.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyPaymentResponse {
    private boolean status;
    private String message;
    private Payment data;
}
