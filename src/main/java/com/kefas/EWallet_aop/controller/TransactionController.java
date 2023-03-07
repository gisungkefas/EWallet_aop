package com.kefas.EWallet_aop.controller;

import com.kefas.EWallet_aop.pojo.ApiResponse;
import com.kefas.EWallet_aop.service.TransactionService;
import com.kefas.EWallet_aop.util.ResponseProvider;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/wallet-transaction")
public class TransactionController {

    private final TransactionService transactionService;
    private final ResponseProvider responseProvider;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Object>> getTransaction(Principal principal, @PathVariable String id) {
        return responseProvider.success(transactionService.fetchTransaction(principal, id));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Object>> getTransactions(Principal principal,
                                                               @RequestParam(value = "page", defaultValue = "0") int page,
                                                               @RequestParam(value = "limit", defaultValue = "5") int limit) {
        return responseProvider.success(transactionService.fetchTransactions(principal, page, limit));
    }

    @GetMapping("/type")
    public ResponseEntity<ApiResponse<Object>> getTransactions(Principal principal,
                                                                     @RequestParam(value = "page", defaultValue = "0") int page,
                                                                     @RequestParam(value = "limit", defaultValue = "5") int limit,
                                                                     @Parameter(description = "CREDIT OR DEBIT")
                                                                     @RequestParam String type) {
        return responseProvider.success(transactionService.fetchTransactions(principal, page, limit, type));
    }


}
