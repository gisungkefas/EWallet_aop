package com.kefas.EWallet_aop.service.Impl;

import com.amazonaws.services.simpleemail.model.VerificationStatus;
import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.exception.WalletException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class WalletChecker {

    public void checkBeforeTransaction(Wallet wallet, PasswordEncoder encoder){
        getWalletStatus(wallet);
        checkDefaultPin(encoder, wallet);
    }

    public void getWalletStatus(Wallet wallet){
        if(wallet.getVerificationStatus().equals(VerificationStatus.Pending))
            throw new WalletException("Kindly wait as your verification is in progress");

        if(!wallet.isVerified())
            throw new WalletException("Kindly validate your wallet");
    }

    public void checkDefaultPin(PasswordEncoder encoder, Wallet wallet){
        boolean isDefault = encoder.matches("0000", wallet.getPin());

        if(isDefault)
            throw new WalletException("Please, change your pin");
    }
}
