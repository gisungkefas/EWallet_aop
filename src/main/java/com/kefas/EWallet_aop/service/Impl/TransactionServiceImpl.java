package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.pojo.wallet.response.WalletTransactionResponse;
import com.kefas.EWallet_aop.service.TransactionService;

import java.security.Principal;
import java.util.List;

public class TransactionServiceImpl implements TransactionService {
    @Override
    public WalletTransactionResponse fetchTransaction(Principal principal, String id) {
        return null;
    }

    @Override
    public List<WalletTransactionResponse> fetchTransactions(Principal principal, int page, int limit) {
        return null;
    }

    @Override
    public List<WalletTransactionResponse> fetchTransactionsByAdmin(Principal principal, int page, int limit) {
        return null;
    }

    @Override
    public List<WalletTransactionResponse> fetchTransactions(Principal principal, int page, int limit, String transactionType) {
        return null;
    }
}
