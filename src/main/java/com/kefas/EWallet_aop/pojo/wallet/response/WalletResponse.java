package com.kefas.EWallet_aop.pojo.wallet.response;

import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.enums.VerificationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class WalletResponse {
    private String uuid;
    private String userUuid;
    private boolean isAccountActive;
    private String walletId;
    private BigDecimal balance;
    private String bvn;
    private boolean isVerified;
    private String customer_code;
    private boolean isBlacklisted;
    private String transactionReference;
    private VerificationStatus verificationStatus;

    public static WalletResponse mapFromWallet(Wallet wallet) {
        if (wallet == null) return null;
        return WalletResponse.builder()
                .uuid(wallet.getUuid())
                .userUuid(wallet.getUserUuid())
                .isAccountActive(wallet.isAccountActive())
                .walletId(wallet.getWalletId())
                .balance(wallet.getBalance())
                .bvn(wallet.getBvn())
                .isVerified(wallet.isVerified())
                .customer_code(wallet.getCustomer_code())
                .isBlacklisted(wallet.isBlacklisted())
                .verificationStatus(wallet.getVerificationStatus())
                .build();
    }

    public static List<WalletResponse> mapFromWallet(List<Wallet> wallets) {
        if (wallets == null) return null;
        return wallets.stream()
                .map(WalletResponse::mapFromWallet)
                .collect(Collectors.toList());
    }
}
