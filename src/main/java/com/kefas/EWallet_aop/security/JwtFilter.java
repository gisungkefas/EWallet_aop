package com.kefas.EWallet_aop.security;

import com.kefas.EWallet_aop.entity.User;
import com.kefas.EWallet_aop.exception.UserNotFoundException;
import com.kefas.EWallet_aop.repository.UserRepository;
import com.kefas.EWallet_aop.util.LocalStorage;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    @Value(value = "${auth.user}")
    private final String AUTH_USER = "AuthUser";

    private final LocalStorage localStorage;
    private final UserRepository userRepository;
    private final  JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String userEmail;
        final String jwtToken;

        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);

        userEmail = jwtUtils.extractUsername(jwtToken);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(()-> new UserNotFoundException("User does not exist"));

            UserPrincipal userPrincipal = new UserPrincipal(user);

            final boolean isTokenValid = jwtUtils.isTokenValid(jwtToken, user.getEmail());

            if (isTokenValid) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userPrincipal.getUsername(), null, userPrincipal.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);

                String tokenKey = AUTH_USER + userEmail;
                localStorage.saveToken(tokenKey, jwtToken.trim());
            } else {
                throw new JwtException("Token is not valid");
            }
        }
        filterChain.doFilter(request, response);
    }
}
