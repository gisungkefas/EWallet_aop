package com.kefas.EWallet_aop.service.Impl;


import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.pojo.user.request.*;
import com.kefas.EWallet_aop.pojo.user.response.LoginResponse;
import com.kefas.EWallet_aop.pojo.user.response.RegisterResponse;
import com.kefas.EWallet_aop.pojo.user.response.UserResponse;
import com.kefas.EWallet_aop.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public class UserServiceImpl implements UserService {

    @Override
    public RegisterResponse createUser(RegisterRequest request) {
        return null;
    }

    @Override
    public UserResponse getUser(Principal principal) {
        return null;
    }

    @Override
    public List<UserResponse> getUsers(Principal principal, int page, int limit) {
        return null;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public String activateUser(ActivationRequest request) {
        return null;
    }

    @Override
    public String resendActivationToken(String email) {
        return null;
    }

    @Override
    public String delete(Principal principal) {
        return null;
    }

    @Override
    public String forgotPassword(String email) {
        return null;
    }

    @Override
    public String resetPassword(PasswordResetRequest request) {
        return null;
    }

    @Override
    public RegisterResponse updateUser(Principal principal, UpdateRequest request) {
        return null;
    }

    @Override
    public String logout(Principal principal) {
        return null;
    }

    @Override
    public String updatePassword(Principal principal, String password) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
