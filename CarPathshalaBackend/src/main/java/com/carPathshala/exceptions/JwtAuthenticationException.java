package com.carPathshala.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class JwtAuthenticationException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
    public JwtAuthenticationException(String message) {
        super(message);
    }
}
