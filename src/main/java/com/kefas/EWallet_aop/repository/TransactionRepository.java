package com.kefas.EWallet_aop.repository;

import com.kefas.EWallet_aop.entity.Transaction;
import com.kefas.EWallet_aop.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.awt.print.Pageable;
import java.util.Optional;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Optional<Transaction> findByUserUuidAndUuidOrTransactionReference(String userUuid, String uuid, String reference);

    Page<Transaction> findByUserUuid(String userUuid, Pageable pageable);

    Page<Transaction> findByUserUuidAndTransactionType(String userUuid, TransactionType transactionType);
}
