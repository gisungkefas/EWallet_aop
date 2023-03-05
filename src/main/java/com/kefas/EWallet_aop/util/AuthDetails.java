package com.kefas.EWallet_aop.util;

import com.kefas.EWallet_aop.annotation.TokenLog;
import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.exception.UserNotFoundException;
import com.kefas.EWallet_aop.exception.ValidationException;
import com.kefas.EWallet_aop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthDetails {
    @Value(value = "${auth.user}")
    private String AUTH_USER;
    @Value(value = "${active.auth.user}")
    private String ACTIVE_AUTH_USER;
    private final UserRepository userRepository;
    private final LocalStorage localStorage;

    public User getAuthorizedUser(Principal principal) {
        if (principal != null) {
            final String email = principal.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(()-> new UserNotFoundException("Kindly, login to access your dashboard."));
        } else{
            throw new UserNotFoundException("Kindly, login to access your dashboard.");
        }
    }

    @TokenLog
    public User validateActiveUser(Principal principal) {
        User user = getAuthorizedUser(principal);

        String activeTokenKey = ACTIVE_AUTH_USER + user.getEmail();
        String tokenKey = AUTH_USER + user.getEmail();

        String activeToken = localStorage.getValueByKey(activeTokenKey);
        String token = localStorage.getValueByKey(tokenKey);

        if (activeToken == null || !activeToken.equals(token))
            throw new ValidationException("Token expired. Kindly, login again.");

        return user;
    }
}
