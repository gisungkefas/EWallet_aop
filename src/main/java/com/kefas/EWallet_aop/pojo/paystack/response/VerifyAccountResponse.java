package com.kefas.EWallet_aop.pojo.paystack.response;

import com.kefas.EWallet_aop.pojo.paystack.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyAccountResponse {
    private boolean status;
    private String message;
    private Account data;
}
