package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRecipient {
    private boolean active;
    private String createdAt;
    private String currency;
    private String domain;
    private long id;
    private long integration;
    private String name;
    private String recipient_code;
    private String reference;
    private String type;
    private String updatedAt;
    private boolean is_deleted;
    private RecipientDetails details;
}
