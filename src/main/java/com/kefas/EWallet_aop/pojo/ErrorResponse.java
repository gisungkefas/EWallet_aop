package com.kefas.EWallet_aop.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import java.sql.Timestamp;
import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {
    private Collection message;
    private HttpStatus error;
    private int code;
    private Timestamp timestamp;
}
