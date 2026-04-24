package com.elhaffar.exoformbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict pour les doublons
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
