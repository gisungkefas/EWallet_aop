package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.pojo.wallet.request.InitiateTransferFromSikabethToWalletRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.PinResetRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.SikabethWalletResponse;
import com.kefas.EWallet_aop.pojo.wallet.request.WalletValidationRequest;
import com.kefas.EWallet_aop.pojo.wallet.response.WalletResponse;
import com.kefas.EWallet_aop.service.WalletService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public class WalletServiceImpl implements WalletService {
    @Override
    public WalletResponse getWallet(Principal principal) {
        return null;
    }

    @Override
    public List<WalletResponse> getWallets(Principal principal, int page, int limit) {
        return null;
    }

    @Override
    public WalletResponse transferMoneyFromWalletToWallet(Principal principal, String recipientWalletId, String amount, String reason, String pin) {
        return null;
    }

    @Override
    public WalletResponse payService(Principal principal, String serviceName, BigDecimal amount, String pin) {
        return null;
    }

    @Override
    public String validateWallet(Principal principal, WalletValidationRequest request) {
        return null;
    }

    @Override
    public String resetPin(Principal principal, PinResetRequest request) {
        return null;
    }

    @Override
    public SikabethWalletResponse initializeTransaction(Principal principal, InitiateTransferFromSikabethToWalletRequest request) {
        return null;
    }

    @Override
    public WalletResponse verifyTransaction(Principal principal, String reference, String transfer_code) {
        return null;
    }
}
