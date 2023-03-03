package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private long id;
    private String bank;
    private String slug;
    private String code;
    private String longcode;
    private String gateway;
    private boolean pay_with_bank;
    private boolean active;
    private boolean is_deleted;
    private String country;
    private String currency;
    private String type;
}
