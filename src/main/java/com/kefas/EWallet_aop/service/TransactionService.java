package com.kefas.EWallet_aop.service;

import com.kefas.EWallet_aop.pojo.wallet.response.WalletTransactionResponse;

import java.security.Principal;
import java.util.List;

public interface TransactionService {

    WalletTransactionResponse fetchTransaction(Principal principal, String id);
    List<WalletTransactionResponse> fetchTransactions(Principal principal, int page, int limit);

    List<WalletTransactionResponse> fetchTransactionsByAdmin(Principal principal, int page, int limit);
    List<WalletTransactionResponse> fetchTransactions(Principal principal, int page, int limit, String transactionType);
}