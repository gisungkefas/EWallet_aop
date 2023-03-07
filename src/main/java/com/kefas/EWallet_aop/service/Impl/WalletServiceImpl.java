package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.entity.Payment;
import com.kefas.EWallet_aop.entity.Transaction;
import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.enums.TransactionSource;
import com.kefas.EWallet_aop.enums.TransactionType;
import com.kefas.EWallet_aop.enums.VerificationStatus;
import com.kefas.EWallet_aop.exception.WalletException;
import com.kefas.EWallet_aop.pojo.paystack.request.SetUpTransactionRequest;
import com.kefas.EWallet_aop.pojo.paystack.response.CustomerValidationResponse;
import com.kefas.EWallet_aop.pojo.paystack.response.SetUpTransactionResponse;
import com.kefas.EWallet_aop.pojo.paystack.response.VerifyPaymentResponse;
import com.kefas.EWallet_aop.pojo.wallet.request.InitiateTransferFromSikabethToWalletRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.PinResetRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.SikabethWalletResponse;
import com.kefas.EWallet_aop.pojo.wallet.request.WalletValidationRequest;
import com.kefas.EWallet_aop.pojo.wallet.response.WalletResponse;
import com.kefas.EWallet_aop.repository.PaymentRepository;
import com.kefas.EWallet_aop.repository.TransactionRepository;
import com.kefas.EWallet_aop.repository.WalletRepository;
import com.kefas.EWallet_aop.service.PaymentService;
import com.kefas.EWallet_aop.service.WalletService;
import com.kefas.EWallet_aop.util.AmazonSES;
import com.kefas.EWallet_aop.util.AuthDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final AuthDetails authDetails;

    private final WalletRepository walletRepository;

    private final PaymentRepository paymentRepository;

    private final TransactionRepository transactionRepository;

    private final PasswordEncoder passwordEncoder;

    private final PaymentService paymentService;

    private final WalletChecker walletChecker;

    private final AmazonSES amazonSES;

    @Override
    public WalletResponse getWallet(Principal principal) {
        return WalletResponse.mapFromWallet(getUserWallet(principal));
    }

    @Override
    public List<WalletResponse> getWallets(Principal principal, int page, int limit) {
        if(page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, limit);
        Page<Wallet> pagedWallets = walletRepository.findAll(pageable);
        List<Wallet> wallets = pagedWallets.getContent();

        return WalletResponse.mapFromWallet(wallets);
    }

    @Override
    public WalletResponse transferMoneyFromWalletToWallet(Principal principal, String recipientWalletId, String amount, String reason, String pin) {
        Wallet wallet = checksBeforeTransaction(principal);
        Wallet recipientWallet = findWallet(recipientWalletId);

        if (new BigDecimal(amount).compareTo(BigDecimal.ZERO) <= 0) {
            throw new WalletException("Amount is less or equal than 0");
        }

        BigDecimal newUserBalance = wallet.getBalance().subtract(new BigDecimal(amount));
        BigDecimal newRecipientBalance = recipientWallet.getBalance().add(new BigDecimal(amount));

        if (newUserBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletException("Insufficient balance");
        }

        //CHECK PIN
        if (!passwordEncoder.matches(pin, wallet.getPin())) {
            throw new WalletException("Pin did not match");
        }

        wallet.setBalance(newUserBalance);
        Wallet updatedUserWallet = walletRepository.save(wallet);

        recipientWallet.setBalance(newRecipientBalance);
        walletRepository.save(recipientWallet);

        Transaction userTransaction = createTransaction(wallet.getUserUuid(), reason, wallet.getWalletId(), recipientWalletId,
                new BigDecimal(amount), newUserBalance, TransactionType.DEBIT, TransactionSource.WALLET);
        transactionRepository.save(userTransaction);

        Transaction recipientTransaction = createTransaction(recipientWallet.getUserUuid(), reason, wallet.getWalletId(),
                recipientWalletId, new BigDecimal(amount), newRecipientBalance, TransactionType.CREDIT, TransactionSource.WALLET);
        transactionRepository.save(recipientTransaction);

        //SEND TWO PEOPLE SMS
        String userMessage = "<div>You transferred from your " + wallet.getWalletId() +
                " to " + recipientWallet.getWalletId() + ". Your account balance is " + newUserBalance + ".</div>";
        amazonSES.sendTransaction(userMessage, wallet.getEmail());

        String recipientMessage = "<div>" + wallet.getEmail() + " transferred from " + wallet.getWalletId() +
                " to your account and your new balance is " + newRecipientBalance + ".</div>";
        amazonSES.sendTransaction(recipientMessage, recipientWallet.getEmail());

        WalletResponse walletResponse = WalletResponse.mapFromWallet(updatedUserWallet);
        walletResponse.setTransactionReference(userTransaction.getTransactionReference());

        return walletResponse;
    }

    @Override
    public WalletResponse payService(Principal principal, String serviceName, BigDecimal amount, String pin) {
        Wallet wallet = checksBeforeTransaction(principal);
        return null;
    }

    @Override
    public String validateWallet(Principal principal, WalletValidationRequest request) {
        Wallet wallet = getUserWallet(principal);

        CustomerValidationResponse response = paymentService.validateCustomer(request);
        if(response.isStatus() && response.getMessage().equals("Customer identification in progress")){
            wallet.setVerificationStatus(VerificationStatus.CONFIRMED);
            wallet.setBvn(request.getBvn());
            wallet.setVerified(true);
            walletRepository.save(wallet);
        }

        return response.getMessage();
    }

    @Override
    public String resetPin(Principal principal, PinResetRequest request) {
        Wallet wallet = getUserWallet(principal);
        boolean oldPin = passwordEncoder.matches(request.getOldPin(), wallet.getPin());
        if(!oldPin)
            throw new WalletException("Old pin does not match");

        boolean newPin = request.getNewPin().equals(request.getConfirmNewPin());
        if(!newPin)
            throw new WalletException("New pin and confirm new pin does not match");

        wallet.setPin(passwordEncoder.encode(request.getNewPin()));
        walletRepository.save(wallet);

        return "Pin change successfully";
    }

    @Override
    public SikabethWalletResponse initializeTransaction(Principal principal, InitiateTransferFromSikabethToWalletRequest request) {
        User user = checksBeforeWalletTransfer(principal);

        SetUpTransactionRequest setUpTransactionRequest = InitiateTransferFromSikabethToWalletRequest
                .mapToSetUpTransactionRequest(request.getAmount(), user.getEmail());

        SetUpTransactionResponse response = paymentService.initializeTransaction(principal, setUpTransactionRequest);
        if(!response.isStatus()){
            throw new WalletException("Transaction initiation failed, please try again ");
        }

        String transfer_code = String.valueOf(System.currentTimeMillis());
        SikabethWalletResponse result = SikabethWalletResponse.mapFromSetUpTransactionResponse(
                response.isStatus(), response.getMessage(), user.getEmail(), "Sikabeth",
                new BigDecimal(request.getAmount()), user.getWalletId(), transfer_code,
                response.getData().getReference(), response.getData().getAccess_code()
        );

        Payment payment = createPayment(result);
        paymentRepository.save(payment);

        return result;
    }

    @Override
    public WalletResponse verifyTransaction(Principal principal, String reference, String transfer_code) {
        User user = checksBeforeWalletTransfer(principal);
        Payment payment = paymentRepository.findByTransferCode(transfer_code)
                .orElseThrow(() -> new WalletException("Payment does not exist"));

        VerifyPaymentResponse response = paymentService.verifyTransaction(reference);
        if (!response.isStatus()) {
            throw new WalletException("Verification failed try again");
        }

        payment.setConfirmed(true);
        paymentRepository.save(payment);

        Wallet wallet = findWallet(user);
        BigDecimal fromSikabeth = new BigDecimal(String.valueOf(response.getData().getAmount()));
        BigDecimal balance = findWallet(user).getBalance().add(fromSikabeth);

        Transaction transaction = createTransaction(user.getUuid(), "Sikabeth transfer to your wallet.", "Sikabeth",
                wallet.getWalletId(), fromSikabeth, balance, TransactionType.CREDIT, TransactionSource.BANK);
        transactionRepository.save(transaction);

        wallet.setBalance(balance);
        Wallet updatedWallet = walletRepository.save(wallet);

        String message = "<div>Sikabeth transferred " + fromSikabeth +
                " to your account and your new balance is " + updatedWallet.getBalance() + ".</div>";
        amazonSES.sendTransaction(message, user.getEmail());

        WalletResponse walletResponse = WalletResponse.mapFromWallet(updatedWallet);
        walletResponse.setTransactionReference(transaction.getTransactionReference());
        return walletResponse;
    }

    private Wallet getUserWallet(Principal principal){
        User user = authDetails.validateActiveUser(principal);
        return findWallet(user);
    }

    private Wallet findWallet(User user){
        return walletRepository.findByWalletId(user.getWalletId())
                .orElseThrow(() -> new WalletException("The recipient wallet does not exist"));
    }

    private Wallet checksBeforeTransaction(Principal principal){
        Wallet wallet = getUserWallet(principal);
        walletChecker.checkBeforeTransaction(wallet, passwordEncoder);

        return  wallet;
    }

    private Transaction createTransaction(
            String userUuid,
            String reason,
            String from,
            String to,
            BigDecimal amount,
            BigDecimal balance,
            TransactionType transactionType,
            TransactionSource transactionSource
    ) {
        return Transaction.builder()
                .userUuid(userUuid)
                .uuid(UUID.randomUUID().toString())
                .reason(reason)
                .from(from)
                .to(to)
                .amount(amount)
                .balance(balance)
                .transactionReference(String.valueOf(System.currentTimeMillis()))
                .transactionType(transactionType)
                .transactionSource(transactionSource)
                .build();
    }

    private User checksBeforeWalletTransfer(Principal principal){
        User user = authDetails.validateActiveUser(principal);
        Wallet wallet = findWallet(user);
        walletChecker.checkBeforeTransaction(wallet, passwordEncoder);

        return  user;
    }

    private Payment createPayment(SikabethWalletResponse response) {
        return Payment.builder()
                .uuid(UUID.randomUUID().toString())
                .walletId(response.getWalletId())
                .to(response.getTo())
                .from(response.getFrom())
                .amount(response.getAmount())
                .confirmed(false)
                .reference(response.getReference())
                .accessCode(response.getAccessCode())
                .transferCode(response.getTransfer_code())
                .build();
    }

}
