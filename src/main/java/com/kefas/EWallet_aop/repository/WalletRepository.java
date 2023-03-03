package com.kefas.EWallet_aop.repository;

import com.kefas.EWallet_aop.entity.Wallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<Wallet, String> {

    Optional<Wallet> findByWalletId(String walletId);
 }
