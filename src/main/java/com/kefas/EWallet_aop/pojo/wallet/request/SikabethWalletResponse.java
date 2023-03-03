package com.kefas.EWallet_aop.pojo.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SikabethWalletResponse {
    private boolean status;
    private String message;
    private String to;
    private String from;
    private String walletId;
    private String transfer_code;
    private String reference;
    private String accessCode;
    private String authorization_url;
    private BigDecimal amount;

    public static SikabethWalletResponse mapFromSetUpTransactionResponse(boolean status,
                                                                         String message,
                                                                         String to,
                                                                         String from,
                                                                         BigDecimal amount,
                                                                         String walletId,
                                                                         String transfer_code,
                                                                         String reference,
                                                                         String accessCode) {
        return SikabethWalletResponse.builder()
                .status(status)
                .message(message)
                .to(to)
                .from(from)
                .walletId(walletId)
                .transfer_code(transfer_code)
                .reference(reference)
                .accessCode(accessCode)
                .authorization_url("https://checkout.paystack.com/" + accessCode)
                .amount(amount)
                .build();
    }
}
