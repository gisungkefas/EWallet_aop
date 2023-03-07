package com.kefas.EWallet_aop.controller;

import com.kefas.EWallet_aop.pojo.ApiResponse;
import com.kefas.EWallet_aop.pojo.wallet.request.InitiateTransferFromSikabethToWalletRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.PinResetRequest;
import com.kefas.EWallet_aop.pojo.wallet.request.WalletValidationRequest;
import com.kefas.EWallet_aop.service.WalletService;
import com.kefas.EWallet_aop.util.ResponseProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wallet")
public class WalletController {

    private final WalletService walletService;
    private final ResponseProvider responseProvider;

    @GetMapping("/id")
    public ResponseEntity<ApiResponse<Object>> getWallet(Principal principal) {
        return responseProvider.success(walletService.getWallet(principal));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> getWallets(Principal principal,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return responseProvider.success(walletService.getWallets(principal, page, limit));
    }

    @PostMapping("/transfer-money")
    public ResponseEntity<ApiResponse<Object>> transferMoney(Principal principal,
                                                             @RequestParam String recipientWalletId,
                                                             @RequestParam String amount,
                                                             @RequestParam String reason,
                                                             @RequestParam String pin) {
        return responseProvider.success(walletService.transferMoneyFromWalletToWallet(principal, recipientWalletId, amount, reason, pin));
    }

    @PostMapping("/pay-service")
    public ResponseEntity<ApiResponse<Object>> payService(Principal principal,
                                                             @RequestParam String serviceName,
                                                             @RequestParam BigDecimal amount,
                                                             @RequestParam String pin) {
        return responseProvider.success(walletService.payService(principal, serviceName, amount, pin));
    }

    @PostMapping("/validate")
    public ResponseEntity<ApiResponse<Object>> validateWallet(Principal principal, @RequestBody WalletValidationRequest request) {
        return responseProvider.success(walletService.validateWallet(principal, request));
    }

    @PostMapping("/reset-pin")
    public ResponseEntity<ApiResponse<Object>> resetPin(Principal principal, @RequestBody PinResetRequest request) {
        return responseProvider.success(walletService.resetPin(principal, request));
    }

    @Operation(summary = "THIS ENDPOINT INITIATES A TRANSFER VIA SIKABETH TO YOUR WALLET. ")
    @PostMapping("/initialize-payment-to-wallet-via-sikabeth")
    public ResponseEntity<ApiResponse<Object>> setUpTransaction(Principal principal,
                                                                InitiateTransferFromSikabethToWalletRequest request
    ) {
        return responseProvider.success(walletService.initializeTransaction(principal, request));
    }

    @Operation(summary = "VERIFY TRANSFER TO WALLET VIA SIKABETH. YOU NEED TO SUBMIT reference AND transfer_code.")
    @GetMapping("/verify-reference-payment-to-wallet-via-sikabeth")
    public ResponseEntity<ApiResponse<Object>> verifyPayment(Principal principal,
                                                             @Parameter(description = "This is the reference number generated when the transaction was initiated.")
                                                             @RequestParam String reference,
                                                             @Parameter(description = "This is the transfer_code number generated when the transaction was initiated.")
                                                             @RequestParam String transfer_code
    ) {
        return responseProvider.success(walletService.verifyTransaction(principal, reference, transfer_code));
    }


}
