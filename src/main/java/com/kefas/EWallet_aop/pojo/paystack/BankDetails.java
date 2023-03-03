package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDetails {
    private String account_number;
    private String account_name;
    private String bank_code;
    private String bank_name;
}
