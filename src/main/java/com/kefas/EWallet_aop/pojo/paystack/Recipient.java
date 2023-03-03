package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recipient {
    private String domain;
    private String type;
    private String currency;
    private String name;
    private BankDetails details;
}
