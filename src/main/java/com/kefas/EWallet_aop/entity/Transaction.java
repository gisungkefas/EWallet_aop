package com.kefas.EWallet_aop.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kefas.EWallet_aop.enums.TransactionSource;
import com.kefas.EWallet_aop.enums.TransactionType;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@Document("transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction extends Base{

    @Indexed(unique = true)
    private String uuid;

    private String userUuid;

    @Column(nullable = false, length = 50)
    private String reason;

    @Column(nullable = false, length = 50)
    private String from;

    @Column(nullable = false, length = 50)
    private String to;

    private BigDecimal amount;

    private BigDecimal balance;

    @Column(nullable = false, length = 50)
    private String transactionReference;

    private TransactionType transactionType;

    private TransactionSource transactionSource;
}
