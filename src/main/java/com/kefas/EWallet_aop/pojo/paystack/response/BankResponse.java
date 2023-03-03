package com.kefas.EWallet_aop.pojo.paystack.response;

import com.kefas.EWallet_aop.pojo.paystack.Bank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    private boolean status;
    private String message;
    private List<Bank> data;
}
