package com.kefas.EWallet_aop.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class PayStackHttpEntity<T> {
    @Value("${secret.key}")
    private String SECRET_KEY;
    @Value("${secret.key1}")
    private String SECRET_KEY1;

    public HttpEntity<T> getEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + SECRET_KEY);
        return new HttpEntity<>(headers);
    }

    public HttpEntity<T> getEntity1() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + SECRET_KEY1);
        return new HttpEntity<>(headers);
    }

    public HttpEntity<T> getEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + SECRET_KEY);
        return new HttpEntity<>(body, headers);
    }

    public HttpEntity<T> getEntity1(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + SECRET_KEY1);
        return new HttpEntity<>(body, headers);
    }
}
