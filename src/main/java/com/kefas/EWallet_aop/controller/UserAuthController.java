package com.kefas.EWallet_aop.controller;

import com.kefas.EWallet_aop.pojo.ApiResponse;
import com.kefas.EWallet_aop.pojo.user.request.ActivationRequest;
import com.kefas.EWallet_aop.pojo.user.request.LoginRequest;
import com.kefas.EWallet_aop.pojo.user.request.PasswordResetRequest;
import com.kefas.EWallet_aop.pojo.user.request.RegisterRequest;
import com.kefas.EWallet_aop.service.UserService;
import com.kefas.EWallet_aop.util.ResponseProvider;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@AllArgsConstructor
public class UserAuthController {

    private final UserService userService;
    private final ResponseProvider responseProvider;

    @SecurityRequirements
    @PostMapping("/user/register")
    public ResponseEntity<ApiResponse<Object>> createUser(@RequestBody RegisterRequest request){
        return responseProvider.success("Request Successful, kindly check your email to activate account",
                userService.createUser(request));
    }

    @SecurityRequirements
    @PostMapping("/user/login")
    public ResponseEntity<ApiResponse<Object>> authenticateUser(@RequestBody LoginRequest request){
        return responseProvider.success(userService.login(request));
    }

    @SecurityRequirements
    @GetMapping("/echo")
    public ResponseEntity<ApiResponse<Object>> echo(){
        return responseProvider.success("Live and running!!!");
    }

    @SecurityRequirements
    @PostMapping("/user/activate")
    public ResponseEntity<ApiResponse<Object>> activateUser(@RequestBody ActivationRequest request){
        return responseProvider.success(userService.activateUser(request));
    }

    @SecurityRequirements
    @PostMapping("/user/resend-token")
    public ResponseEntity<ApiResponse<Object>> resendUserToken(@RequestParam("email") String email){
        return responseProvider.success(userService.resendActivationToken(email));
    }

    @SecurityRequirements
    @PostMapping("/user/forgot-password")
    public ResponseEntity<ApiResponse<Object>> forgotPassword(@RequestParam("email") String email){
        return responseProvider.success(userService.forgotPassword(email));
    }

    @SecurityRequirements
    @PostMapping("/user/reset-password")
    public ResponseEntity<ApiResponse<Object>> resetPassword(@RequestBody PasswordResetRequest request) {
        return responseProvider.success(userService.resetPassword(request));
    }

}
