package com.kefas.EWallet_aop.repository;

import com.kefas.EWallet_aop.entity.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Optional<Payment> findByTransferCode(String transfer_code);
}
