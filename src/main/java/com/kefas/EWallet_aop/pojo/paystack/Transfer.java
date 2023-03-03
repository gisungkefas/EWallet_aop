package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private long id;
    private String reference;
    private String integration;
    private String domain;
    private BigDecimal amount;
    private String currency;
    private String source;
    private String reason;
    private String recipient;
    private String status;
    private String transfer_code;
    private String createdAt;
    private String updatedAt;
}
