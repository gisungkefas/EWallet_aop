package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VerifyTransfer {
    private long id;
    private long integration;
    private Recipient recipient;
    private String description;
    private String metadata;
    private String recipient_code;
    private boolean active;
    private String email;
    private String createdAt;
    private String updatedAt;
}
