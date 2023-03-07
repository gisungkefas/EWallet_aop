package com.kefas.EWallet_aop.controller;

import com.kefas.EWallet_aop.pojo.ApiResponse;
import com.kefas.EWallet_aop.pojo.paystack.request.AccountRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.SetUpTransactionRequest;
import com.kefas.EWallet_aop.pojo.paystack.request.TransferRequest;
import com.kefas.EWallet_aop.service.PaymentService;
import com.kefas.EWallet_aop.util.ResponseProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/vi/transaction")
@Tag(name = "Payment Controller Endpoint")
public class PaymentController {

    private final PaymentService paymentService;
    private final ResponseProvider responseProvider;

    @Operation(summary = "Initiate a payment to Sikabeth Enterprises.")
    @PostMapping("/initialize-payment-to-sikabeth")
    public ResponseEntity<ApiResponse<Object>> setUpTransaction(Principal principal, SetUpTransactionRequest request) {
        return responseProvider.success(paymentService.initializeTransaction(principal, request));
    }

    @Operation(summary = "After successful initiation of payment to Sikabeth Enterprises. Verify the payment to confirm that payment cycle was successful.")
    @GetMapping("/verify-payment-reference-to-sikabeth")
    public ResponseEntity<ApiResponse<Object>> verifyPayment(Principal principal,
                                                             @Parameter(description = "This is the reference number generated when the transaction was initiated.")
                                                             @RequestParam String reference) {
        return responseProvider.success(paymentService.verifyTransaction(principal, reference));
    }

    @Operation(summary = "Fetch all transactions by a user.")
    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> fetchTransactions(Principal principal) {
        return responseProvider.success(paymentService.fetchTransactions(principal));
    }

    @Operation(summary = "Fetch one transaction by a user.")
    @GetMapping("/id")
    public ResponseEntity<ApiResponse<Object>> fetchTransaction(Principal principal,
                                                                @Parameter(description = "This represents the id of the transaction you want to get.")
                                                                @RequestParam long id) {
        return responseProvider.success(paymentService.fetchTransaction(principal, id));
    }

    @Operation(summary = "Fetch all banks.")
    @GetMapping("/banks")
    public ResponseEntity<ApiResponse<Object>> fetchBanks(Principal principal,
                                                          @Parameter(description = "(Optional) enter 'nuban' or 'mobile_money', etc")
                                                          @RequestParam(value = "currency", defaultValue = "NGN") String currency,
                                                          @Parameter(description = "NGN for Nigeria, GHS for Ghana etc")
                                                          @RequestParam(value = "type", defaultValue = "NGN") String type ) {
        return responseProvider.success(paymentService.fetchBanks(principal, currency, type));
    }

    @Operation(summary = "To transfer money you need to confirm the account of the user.")
    @GetMapping("/verify-account-number")
    public ResponseEntity<ApiResponse<Object>> verifyAccount(Principal principal,
                                                             @Parameter(description = "10 digit account number")
                                                             @RequestParam String accountNumber,
                                                             @Parameter(description = "011 for first bank, 044 - access bank, etc")
                                                             @RequestParam String bankCode) {
        return responseProvider.success(paymentService.verifyAccount(principal, accountNumber, bankCode));
    }

    @Operation(summary = "To do a transfer you have to create a transfer recipient first, before initiating the transfer. " +
            "Use the recipient_code and reference to perform a transfer.")
    @PostMapping("/transfer-recipient")
    public ResponseEntity<ApiResponse<Object>> generateTransferRecipient(Principal principal, AccountRequest request) {
        return responseProvider.success(paymentService.setUpTransferRecipient(principal, request));
    }

    @Operation(summary = "After successfully creating a transfer recipient. Supply recipient_code and reference to perform a transfer." +
            "You will need a reference from this transfer to verify the transfer.")
    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<Object>> initiateTransfer(Principal principal, TransferRequest request, String reference) {
        return responseProvider.success(paymentService.setUpTransfer(principal, request, reference));
    }

    @Operation(summary = "After doing a transfer supply the reference to verify the transfer.")
    @GetMapping("/verify-transfer")
    public ResponseEntity<ApiResponse<Object>> verifyTransfer(Principal principal, String reference) {
        return responseProvider.success(paymentService.verifyTransfer(principal, reference));
    }

}
