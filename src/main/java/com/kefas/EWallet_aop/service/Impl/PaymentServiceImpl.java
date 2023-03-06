package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.exception.PaymentException;
import com.kefas.EWallet_aop.exception.WalletException;
import com.kefas.EWallet_aop.pojo.paystack.Bank;
import com.kefas.EWallet_aop.pojo.paystack.TransferRecipient;
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
import com.mongodb.client.model.geojson.LineString;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

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
        String url = "https://api.paystack.co/transaction/initialize";
        return restTemplate.exchange(
                url, HttpMethod.POST, payStackHttpEntity.getEntity(request),
                SetUpTransactionResponse.class
        ).getBody();
    }

    @Override
    public VerifyPaymentResponse verifyTransaction(Principal principal, String reference) {
        checksBeforeTransaction(principal);
        return verifyTransaction(reference);
    }

    @Override
    public VerifyPaymentResponse verifyTransaction(String reference) {
        String url = "https://api.paystack.co/transaction/verify" + reference;
        return restTemplate.exchange(
                url, HttpMethod.GET,payStackHttpEntity.getEntity(),
                VerifyPaymentResponse.class
        ).getBody();
    }

    @Override
    public TransactionsResponse fetchTransactions(Principal principal) {
        checksBeforeTransaction(principal);

        String url = "https://api.paystack.co/transaction";
        return restTemplate.exchange(
                url, HttpMethod.GET, payStackHttpEntity.getEntity(),
                TransactionsResponse.class
        ).getBody();
    }

    @Override
    public TransactionResponse fetchTransaction(Principal principal, long id) {
        authDetails.validateActiveUser(principal);

        String url = "https://api.paystack.co/transaction/" + id;
        return restTemplate.exchange(
                url, HttpMethod.GET, payStackHttpEntity.getEntity(),
                TransactionResponse.class
        ).getBody();
    }

    @Override
    public BankResponse fetchBanks(Principal principal, String currency, String type) {
        checksBeforeTransaction(principal);

        String url = "https://api.paystack.co/bank?currency" + currency + "&type=" + type;
        BankResponse bankResponse = restTemplate.exchange(
                url, HttpMethod.GET, payStackHttpEntity.getEntity(),
                BankResponse.class
        ).getBody();

        List<Bank> banks = Objects.requireNonNull(bankResponse).getData().stream().filter(Bank::isActive).toList();
        bankResponse.setData(banks);
        return bankResponse;
    }

    @Override
    public VerifyAccountResponse verifyAccount(Principal principal, String accountNumber, String bankCode) {
        checksBeforeTransaction(principal);

        String url = "https://api.paystack.co/bank/resolve?account_number=" + accountNumber + "&bank_code=" + bankCode;
        return restTemplate.exchange(
                url, HttpMethod.GET, payStackHttpEntity.getEntity(),
                VerifyAccountResponse.class
        ).getBody();
    }

    @Override
    public TransferRecipientResponse setUpTransferRecipient(Principal principal, AccountRequest request) {
        User user = checksBeforeTransaction(principal);

        String url = "https://api.paystack.co/transferrecipient";
        TransferRecipientResponse transferRecipientResponse = restTemplate.exchange(
                url, HttpMethod.POST, payStackHttpEntity.getEntity(),
                TransferRecipientResponse.class
        ).getBody();

        String reference = util.generateSerialNumber("Tsf") + "Ref";
        TransferRecipient transferRecipient = Objects.requireNonNull(transferRecipientResponse).getData();
        transferRecipient.setReference(reference);
        localStorage.save(TRANSFER_RECIPIENT + user.getEmail(), reference, 3600);
        transferRecipientResponse.setData(transferRecipient);
        return transferRecipientResponse;
    }

    @Override
    public TransferResponse setUpTransfer(Principal principal, TransferRequest request, String reference) {
        User user = checksBeforeTransaction(principal);

        String storedReference = localStorage.getValueByKey(TRANSFER_RECIPIENT + user.getEmail());
        if((reference == null || storedReference == null) ||!reference.equals(storedReference)){
            throw new PaymentException("Transaction session has expired or reference does not exist. " +
                    "Kindly initiate a new transfer");
        }
        localStorage.clear(TRANSFER_RECIPIENT + user.getEmail());

        String url = "https://api.paystack.co/transfer";
        return restTemplate.exchange(
                url, HttpMethod.POST, payStackHttpEntity.getEntity(),
                TransferResponse.class
        ).getBody();
    }

    @Override
    public VerifyTransferResponse verifyTransfer(Principal principal, String reference) {
        checksBeforeTransaction(principal);

        String url = "https://api.paystack.co/transfer/verify/" + reference;
        VerifyTransferResponse response = restTemplate.exchange(
                url, HttpMethod.GET, payStackHttpEntity.getEntity(),
                VerifyTransferResponse.class
        ).getBody();

        return response;
    }

    @Override
    public CreateCustomerResponse createCustomer(CreateCustomerRequest request) {
        String url = "https://pai.paystack.co/customer";
        return restTemplate.exchange(
                url, HttpMethod.POST, payStackHttpEntity.getEntity(request),
                CreateCustomerResponse.class
        ).getBody();
    }

    @Override
    public CustomerValidationResponse validateCustomer(WalletValidationRequest request) {
        String url = "https://api.paystack.co/customer/" + request.getCustomer_code() + "/identification";
        return restTemplate.exchange(
                url, HttpMethod.POST, payStackHttpEntity.getEntity(request),
                CustomerValidationResponse.class
        ).getBody();
    }

    public User checksBeforeTransaction(Principal principal){
        User user = authDetails.validateActiveUser(principal);
        Wallet wallet = walletRepository.findByWalletId(user.getWalletId())
                .orElseThrow(() -> new WalletException("Wallet does not exist."));
        walletChecker.checkBeforeTransaction(wallet, encoder);
        return user;
    }
}
