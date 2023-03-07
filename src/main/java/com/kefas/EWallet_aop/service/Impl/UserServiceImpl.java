package com.kefas.EWallet_aop.service.Impl;


import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.entity.Wallet;
import com.kefas.EWallet_aop.enums.Status;
import com.kefas.EWallet_aop.enums.VerificationStatus;
import com.kefas.EWallet_aop.exception.AuthenticationException;
import com.kefas.EWallet_aop.exception.UserNotFoundException;
import com.kefas.EWallet_aop.exception.ValidationException;
import com.kefas.EWallet_aop.pojo.Mapper;
import com.kefas.EWallet_aop.pojo.paystack.response.CreateCustomerResponse;
import com.kefas.EWallet_aop.pojo.user.request.*;
import com.kefas.EWallet_aop.pojo.user.response.LoginResponse;
import com.kefas.EWallet_aop.pojo.user.response.RegisterResponse;
import com.kefas.EWallet_aop.pojo.user.response.UserResponse;
import com.kefas.EWallet_aop.repository.UserRepository;
import com.kefas.EWallet_aop.repository.WalletRepository;
import com.kefas.EWallet_aop.security.JwtUtils;
import com.kefas.EWallet_aop.security.UserPrincipal;
import com.kefas.EWallet_aop.service.PaymentService;
import com.kefas.EWallet_aop.service.UserService;
import com.kefas.EWallet_aop.util.AmazonSES;
import com.kefas.EWallet_aop.util.AppUtil;
import com.kefas.EWallet_aop.util.AuthDetails;
import com.kefas.EWallet_aop.util.LocalStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Value(value = "UserEmailActivate")
    private String USER_EMAIL_ACTIVATE;

    @Value(value = "UserPasswordForgot")
    private String USER_PASSWORD_FORGOT;

    @Value(value = "AuthUser")
    private String AUTH_USER;

    @Value(value = "ActiveAuthUser")
    private String ACTIVE_AUTH_USER;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtils jwtUtils;

    private final WalletRepository walletRepository;

    private final LocalStorage localStorage;

    private final AuthDetails authDetails;

    private final AppUtil util;

    private final AuthenticationManager authenticationManager;

    private final AmazonSES amazonSES;

    private final PaymentService paymentService;

    @Override
    public RegisterResponse createUser(RegisterRequest request) {
        if(!util.validEmail(request.getEmail()))
            throw new ValidationException("Invalid email address");
        boolean userExist = userRepository.existsByEmail(request.getEmail());

        if(userExist)
            throw new ValidationException("User already exist");

        User newUser = Mapper.mapUser(request);

        String token = util.generateSerialNumber("A");
        localStorage.save(USER_EMAIL_ACTIVATE + request.getEmail(), token, 432000);
        amazonSES.verifyEmail(token, request.getEmail());

        User createdUser = userRepository.save(newUser);
        return RegisterResponse.mapFromUser(createdUser);
    }

    @Override
    public UserResponse getUser(Principal principal) {
        User user = authDetails.validateActiveUser(principal);

        Wallet wallet = getWallet(user.getWalletId());
        return UserResponse.mapFromData(user, wallet);
    }

    @Override
    public List<UserResponse> getUsers(Principal principal, int page, int limit) {
        authDetails.validateActiveUser(principal);

        if(page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, limit);

        Page<User> pagedUser = userRepository.findAll(pageable);
        List<User> users = pagedUser.getContent();

        Page<Wallet> pagedWallet = walletRepository.findAll(pageable);
        List<Wallet> wallets = pagedWallet.getContent();

        return UserResponse.mapFromData(users, wallets);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = getUser(request.getEmail());

        if(user.getStatus().name().equals(Status.INACTIVE.name()))
            throw new ValidationException("Kindly activate your account");

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        if(!authentication.isAuthenticated()){
            throw new AuthenticationException("Invalid Username or Passwor");
        }

        user.setLastLoginDate(new Date());
        userRepository.save(user);

        String token = jwtUtils.generateToken(user.getEmail());

        String activateTokenKey = ACTIVE_AUTH_USER + request.getEmail();
        localStorage.saveToken(activateTokenKey, token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return LoginResponse.mapFromData(token, user.getEmail());
    }

    @Override
    public String activateUser(ActivationRequest request) {
        validateEmail(request.getEmail());

        User user = getUser(request.getEmail());

        if(user.getStatus() == Status.ACTIVE)
            return "Account already activated";

        String systemToken = localStorage.getValueByKey(USER_EMAIL_ACTIVATE + request.getEmail());

        if(systemToken == null)
            throw new ValidationException("Token expired");

        if(!systemToken.equalsIgnoreCase(request.getToken()))
            throw new ValidationException("Invalid token");

        CreateCustomerResponse response = paymentService.createCustomer(Mapper.mapFromUser(user));
        if(!response.isStatus())
            throw new RuntimeException("Something went wrong please try again");

        String customer_code = response.getData().getCustomer_code();
        log.info(customer_code);
        Wallet wallet = generateWallet();
        wallet.setCustomer_code(customer_code);
        wallet.setEmail(user.getEmail());
        wallet.setUserUuid(user.getUuid());
        wallet.setAccountActive(true);
        walletRepository.save(wallet);

        user.setWalletId(wallet.getWalletId());
        user.setStatus(Status.ACTIVE);
        user.setUpdatedDate(new Date());
        userRepository.save(user);

        localStorage.clear(USER_EMAIL_ACTIVATE + request.getEmail());

        return "Account successfully activated";
    }

    @Override
    public String resendActivationToken(String email) {
        validateEmail(email);
        User user = getUser(email);

        if(user.getStatus() == Status.ACTIVE)
            return "Account already activated";

        String token = util.generateSerialNumber("A");
        localStorage.save(USER_EMAIL_ACTIVATE + email, token, 432000);
        amazonSES.verifyEmail(token, email);

        return "Token has been sent successfully";
    }

    @Override
    public String delete(Principal principal) {
        User user = authDetails.validateActiveUser(principal);
        userRepository.delete(user);

        return "User deleted successfully";
    }

    @Override
    public String forgotPassword(String email) {
        validateEmail(email);
        User user = getUser(email);

        String token = util.generateSerialNumber("F");
        localStorage.save(USER_PASSWORD_FORGOT + email, token, 432000);
        amazonSES.sendPasswordResetRequest(user.getFirstName(), token, email);

        return "Token has been sent successfully";
    }

    @Override
    public String resetPassword(PasswordResetRequest request) {
        validateEmail(request.getEmail());
        User user = getUser(request.getEmail());

        if(!request.getPassword().equals(request.getConfirmPassword())){
            throw new ValidationException("Password do not match");
        }

        user.setEncryptedPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
        userRepository.save(user);

        return "Password reset successfully";
    }

    @Override
    public RegisterResponse updateUser(Principal principal, UpdateRequest request) {
        User user = authDetails.validateActiveUser(principal);
        User updatedUser = userRepository.save(Mapper.mapUser(user, request));

        return RegisterResponse.mapFromUser(updatedUser);
    }

    @Override
    public String logout(Principal principal) {
        User user = authDetails.validateActiveUser(principal);

        String activateTokenKey = ACTIVE_AUTH_USER + user.getEmail();
        String tokenKey = AUTH_USER + user.getEmail();

        localStorage.clear(activateTokenKey);
        localStorage.clear(tokenKey);
        return "Logged out successfully";
    }

    @Override
    public String updatePassword(Principal principal, String password) {
        User user = authDetails.validateActiveUser(principal);
        user.setEncryptedPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "Password updated successfully";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return new UserPrincipal(getUser(username));
    }

    private User getUser(String email){
        return  userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User does not exist."));
    }

    private Wallet getWallet(String walletId){
        return walletRepository.findByWalletId(walletId)
                .orElse(null);
    }

    private void validateEmail(String email){
        if(!util.validImage(email))
            throw new ValidationException("Invalid email address");
    }

    private Wallet generateWallet(){
        return Wallet.builder()
                .uuid(UUID.randomUUID().toString())
                .walletId(UUID.randomUUID().toString())
                .pin(passwordEncoder.encode("0000"))
                .balance(BigDecimal.ZERO)
                .isVerified(false)
                .bvn(null)
                .verificationStatus(VerificationStatus.NOT_CONFIRMED)
                .isBlacklisted(false)
                .build();
    }
}
