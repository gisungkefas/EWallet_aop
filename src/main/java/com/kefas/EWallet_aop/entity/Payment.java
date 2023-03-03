package com.kefas.EWallet_aop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@Document("payments")
@AllArgsConstructor
@NoArgsConstructor
public class Payment extends Base{

    @Indexed(unique = true)
    private String uuid;

    @Column(nullable = false, length = 50)
    private String walletId;

    @Column(nullable = false, length = 50)
    private String from;

    @Column(nullable = false, length = 50)
    private String to;

    private boolean confirmed;

    @Column(nullable = false, length = 50)
    private BigDecimal amount;

    @Column(nullable = false)
    private String transferCode;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false)
    private String accessCode;
}