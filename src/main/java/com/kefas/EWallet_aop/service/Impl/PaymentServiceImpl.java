package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.exception.WalletException;
import com.kefas.EWallet_aop.pojo.paystack.request.AccountRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.CreateCustomerRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.SetUpTransactionRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.TransferRequest;
import com.kefas.EWallet_aop.pojo.paystack.response.*;
import com.kefas.EWallet_aop.pojo.wallet.request.WalletValidationRequest;
import com.kefas.EWallet_aop.repository.WalletRepository;
import com.kefas.EWallet_aop.service.PaymentService;
import com.kefas.EWallet_aop.util.AppUtil;
import com.kefas.EWallet_aop.util.AuthDetails;
import com.kefas.EWallet_aop.util.LocalStorage;
import com.kefas.EWallet_aop.util.PayStackHttpEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Value(value = "UserEmailTransferRecipient")
    private String TRANSFER_RECIPIENT;
    private final PayStackHttpEntity payStackHttpEntity;
    private final RestTemplate restTemplate;
    private AppUtil util;
    private final AuthDetails authDetails;
    private final LocalStorage localStorage;
    private final WalletRepository walletRepository;
    private final WalletChecker walletChecker;
    private final PasswordEncoder encoder;
    @Override
    public SetUpTransactionResponse initializeTransaction(Principal principal, SetUpTransactionRequest request) {
        checksBeforeTransaction(principal);
        return initializeTransaction(request);
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

    public User checksBeforeTransaction(Principal principal){
        User user = authDetails.validateActiveUser(principal);
        Wallet wallet = walletRepository.findByWalletId(user.getWalletId())
                .orElseThrow(() -> new WalletException("Wallet does not exist."));
        walletChecker.checkBeforeTransaction(wallet, encoder);
        return user;
    }
}
