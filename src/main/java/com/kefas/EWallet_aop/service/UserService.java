package com.kefas.EWallet_aop.service;

import com.kefas.EWallet_aop.pojo.user.request.*;
import com.kefas.EWallet_aop.pojo.user.response.LoginResponse;
import com.kefas.EWallet_aop.pojo.user.response.RegisterResponse;
import com.kefas.EWallet_aop.pojo.user.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

public interface UserService extends UserDetailsService {

    RegisterResponse createUser(RegisterRequest request);
    UserResponse getUser(Principal principal);
    List<UserResponse> getUsers(Principal principal, int page, int limit);
    LoginResponse login(LoginRequest request);
    String activateUser(ActivationRequest request);
    String resendActivationToken(String email);
    String delete(Principal principal);
    String forgotPassword(String email);
    String resetPassword(PasswordResetRequest request);
    RegisterResponse updateUser(Principal principal, UpdateRequest request);
    String logout(Principal principal);
    String updatePassword(Principal principal, String password);


}
