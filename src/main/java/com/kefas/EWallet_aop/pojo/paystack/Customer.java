package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private long id;
    private String first_name;
    private String last_name;
    private String email;
    private String customer_code;
    private String phone;
    private String risk_action;
}
