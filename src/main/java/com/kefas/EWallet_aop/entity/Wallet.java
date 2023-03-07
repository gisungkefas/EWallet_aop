package com.kefas.EWallet_aop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kefas.EWallet_aop.enums.VerificationStatus;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@Document("wallets")
@AllArgsConstructor
public class Wallet extends Base{

    @Indexed(unique = true)
    private String uuid;

    private String userUuid;

    private boolean isAccountActive;

    private String email;

    @Indexed(unique = true)
    private String walletId;

    @Column(nullable = false, length = 50)
    private BigDecimal balance;

    @Min(value = 4)
    @Max(value = 4)
    @Column(nullable = false, length = 4)
    private String pin;

    private String bvn;

    private boolean isVerified;

    private VerificationStatus verificationStatus;

    private String customer_code;

    private boolean isBlacklisted;

    public Wallet(){
        super();
    }
}
