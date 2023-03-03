package com.kefas.EWallet_aop.service;

import com.kefas.EWallet_aop.pojo.wallet.request.InitiateTransferFromSikabethToWalletRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.PinResetRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.SikabethWalletResponse;
import com.kefas.EWallet_aop.pojo.wallet.request.WalletValidationRequest;
import com.kefas.EWallet_aop.pojo.wallet.response.WalletResponse;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface WalletService {

    WalletResponse getWallet(Principal principal);
    List<WalletResponse> getWallets(Principal principal, int page, int limit);
    WalletResponse transferMoneyFromWalletToWallet(Principal principal, String recipientWalletId, String amount, String reason, String pin);
    WalletResponse payService(Principal principal, String serviceName, BigDecimal amount, String pin);
    String validateWallet(Principal principal, WalletValidationRequest request);
    String resetPin(Principal principal, PinResetRequest request);
    SikabethWalletResponse initializeTransaction(Principal principal, InitiateTransferFromSikabethToWalletRequest request);
    WalletResponse verifyTransaction(Principal principal, String reference, String transfer_code);

}
