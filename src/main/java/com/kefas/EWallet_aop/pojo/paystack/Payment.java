package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    private long id;
    private String status;
    private String reference;
    private long amount;
    private String message;
    private String gateway_response;
    private String paid_at;
    private String created_at;
    private String channel;
    private String currency;
    private String ip_address;
    private String metadata;
    private String fees;
    private String fees_split;
    private Authorization authorization;
    private Customer customer;

}
