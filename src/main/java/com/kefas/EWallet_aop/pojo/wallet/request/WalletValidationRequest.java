package com.kefas.EWallet_aop.pojo.wallet.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WalletValidationRequest {
    @NotBlank(message = "country must be balance")
    private String country;
    @NotBlank(message = "Transaction Type must be balance")
    private String type;
    @NotBlank(message = "Account Number must be balance")
    private String account_number;
    @NotBlank(message = "BVN must be balance")
    private String bvn;
    @NotBlank(message = "Bank code must be balance")
    private String bank_code;
    @NotBlank(message = "FirstName must be balance")
    private String first_name;
    @NotBlank(message = "LastName must be balance")
    private String last_name;
    @NotBlank(message = "Customer Code must be balance")
    private String customer_code;;
}
