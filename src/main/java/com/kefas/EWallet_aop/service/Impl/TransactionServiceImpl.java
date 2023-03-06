package com.kefas.EWallet_aop.service.Impl;

import com.kefas.EWallet_aop.annotation.Loggable;
import com.kefas.EWallet_aop.entity.Transaction;
import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.enums.TransactionType;
import com.kefas.EWallet_aop.exception.WalletException;
import com.kefas.EWallet_aop.pojo.wallet.response.WalletTransactionResponse;
import com.kefas.EWallet_aop.repository.TransactionRepository;
import com.kefas.EWallet_aop.service.TransactionService;
import com.kefas.EWallet_aop.util.AuthDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AuthDetails authDetails;

    @Loggable
    @Override
    public WalletTransactionResponse fetchTransaction(Principal principal, String id) {
        User user = authDetails.validateActiveUser(principal);
        Transaction transaction = transactionRepository.findByUserUuidAndUuidOrTransactionReference(user.getUuid(), id, id)
                .orElseThrow(() -> new WalletException(""));
        return WalletTransactionResponse.mapFromTransaction(transaction);
    }

    @Loggable
    @Override
    public List<WalletTransactionResponse> fetchTransactions(Principal principal, int page, int limit) {
        User user = authDetails.validateActiveUser(principal);

        Sort sort = Sort.by(Sort.Direction.DESC, "amount");
        if(page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Transaction> pagedTransactions = transactionRepository.findByUserUuid(user.getUuid(), (java.awt.print.Pageable) pageable);

        List<Transaction> transactions = pagedTransactions.getContent();
        return WalletTransactionResponse.mapFromTransaction(transactions);
    }

    @Loggable
    @Override
    public List<WalletTransactionResponse> fetchTransactionsByAdmin(Principal principal, int page, int limit) {
        User user = authDetails.validateActiveUser(principal);
        log.info(user.getUuid());


        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, limit);
        Page<Transaction> pagedTransactions = transactionRepository.findAll(pageable);

        List<Transaction> transactions = pagedTransactions.getContent();
        return WalletTransactionResponse.mapFromTransaction(transactions);

    }

    @Loggable
    @Override
    public List<WalletTransactionResponse> fetchTransactions(Principal principal, int page, int limit, String transactionType) {
        User user = authDetails.validateActiveUser(principal);

        TransactionType type = TransactionType.DEBIT;
        if (TransactionType.CREDIT.name().equalsIgnoreCase(transactionType))
            type = TransactionType.CREDIT;

        if (page > 0) page = page - 1;

        Sort sort = Sort.by(Sort.Direction.DESC, "amount");
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<Transaction> pagedTransactions = transactionRepository.findByUserUuidAndTransactionType(
                user.getUuid(), type, (java.awt.print.Pageable) pageable);

        List<Transaction> transactions = pagedTransactions.getContent();
        return WalletTransactionResponse.mapFromTransaction(transactions);
    }
}
