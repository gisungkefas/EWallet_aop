package com.kefas.EWallet_aop.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kefas.EWallet_aop.enums.Status;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@Builder
@Document("users")
@AllArgsConstructor
public class User extends Base{

    @Indexed(unique = true)
    private String uuid;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Email
    private String email;

    @Column(nullable = false, length = 10)
    private Status status;

    @Column(nullable = false, length = 50)
    private String encryptedPassword;

    @Column(nullable = false, length = 20)
    private String walletId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    @Column(nullable = false, length = 50)
    private String country;

    @Column(nullable = false, length = 50)
    private String state;

    @Column(nullable = false, length = 50)
    private String homeAddress;

    @Column(nullable = false, length = 15)
    private String phoneNumber;
}
