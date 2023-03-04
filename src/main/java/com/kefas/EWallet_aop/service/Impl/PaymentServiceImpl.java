package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.pojo.paystack.request.AccountRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.CreateCustomerRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.SetUpTransactionRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.TransferRequest;
import com.kefas.EWallet_aop.pojo.paystack.response.*;
import com.kefas.EWallet_aop.pojo.wallet.request.WalletValidationRequest;
import com.kefas.EWallet_aop.service.PaymentService;

import java.security.Principal;

public class PaymentServiceImpl implements PaymentService {
    @Override
    public SetUpTransactionResponse initializeTransaction(Principal principal, SetUpTransactionRequest request) {
        return null;
    }

    @Override
    public SetUpTransactionResponse initializeTransaction(SetUpTransactionRequest request) {
        return null;
    }

    @Override
    public VerifyPaymentResponse verifyTransaction(Principal principal, String reference) {
        return null;
    }

    @Override
    public VerifyPaymentResponse verifyTransaction(String reference) {
        return null;
    }

    @Override
    public TransactionsResponse fetchTransactions(Principal principal) {
        return null;
    }

    @Override
    public TransactionResponse fetchTransaction(Principal principal, long id) {
        return null;
    }

    @Override
    public BankResponse fetchBanks(Principal principal, String currency, String type) {
        return null;
    }

    @Override
    public VerifyAccountResponse verifyAccount(Principal principal, String accountNumber, String bankCode) {
        return null;
    }

    @Override
    public TransferRecipientResponse setUpTransferRecipient(Principal principal, AccountRequest request) {
        return null;
    }

    @Override
    public TransferResponse setUpTransfer(Principal principal, TransferRequest request, String reference) {
        return null;
    }

    @Override
    public VerifyTransferResponse verifyTransfer(Principal principal, String reference) {
        return null;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        return null;
    }

    @Override
    public CustomerValidationResponse validateCustomer(WalletValidationRequest request) {
        return null;
    }
}
