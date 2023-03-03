package com.kefas.EWallet_aop.pojo.user.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequest {
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String homeAddress;
    private String phoneNumber;
}
