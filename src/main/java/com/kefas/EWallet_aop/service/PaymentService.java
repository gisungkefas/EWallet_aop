package com.kefas.EWallet_aop.service;

import java.security.Principal;

public interface PaymentService {

    SetUpTransactionResponse initializeTransaction(Principal principal, SetUpTransactionRequest request);
    SetUpTransactionResponse initializeTransaction(SetUpTransactionRequest request);
    VerifyPaymentResponse verifyTransaction(Principal principal, String reference);
    VerifyPaymentResponse verifyTransaction(String reference);
    TransactionsResponse fetchTransactions(Principal principal);
    TransactionResponse fetchTransaction(Principal principal, long id);
    BankResponse fetchBanks(Principal principal, String currency, String type);
    VerifyAccountResponse verifyAccount(Principal principal, String accountNumber, String bankCode);
    TransferRecipientResponse setUpTransferRecipient(Principal principal, AccountRequest request);
    TransferResponse setUpTransfer(Principal principal, TransferRequest request, String reference);
    VerifyTransferResponse verifyTransfer(Principal principal, String reference);
    CreateCustomerResponse createCustomer(CreateCustomerRequest request);
    CustomerValidationResponse validateCustomer(WalletValidationRequest request);

}
