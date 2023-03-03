package com.kefas.EWallet_aop.pojo.paystack.response;

import com.kefas.EWallet_aop.pojo.paystack.PaystackCustomer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CreateCustomerResponse {
    private boolean status;
    private String message;
    private PaystackCustomer data;
}
