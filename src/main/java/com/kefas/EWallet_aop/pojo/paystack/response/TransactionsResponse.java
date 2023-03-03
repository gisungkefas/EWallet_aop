package com.kefas.EWallet_aop.pojo.paystack.response;

import com.kefas.EWallet_aop.pojo.paystack.Pages;
import com.kefas.EWallet_aop.pojo.paystack.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionsResponse {
    private boolean status;
    private String message;
    private List<Payment> data;
    private Pages meta;
}
