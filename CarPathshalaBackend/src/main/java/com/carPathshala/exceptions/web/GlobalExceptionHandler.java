package com.carPathshala.exceptions.web;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.carPathshala.exceptions.InvalidCredentialsException;
import com.carPathshala.exceptions.JwtAuthenticationException;
import com.carPathshala.exceptions.ResourceNotFound;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	// ----------------------------
    //  RESOURCE NOT FOUND
    // ----------------------------
    @ExceptionHandler(ResourceNotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFound ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage(), req.getRequestURI());
    }
    
	// ----------------------------
    //  INVALID CREDENTIALS
    // ----------------------------
	@ExceptionHandler({InvalidCredentialsException.class,BadCredentialsException.class})
	public ResponseEntity<ErrorResponse> handleInvalidCredentials(Exception ex, HttpServletRequest req ){
		return build(HttpStatus.UNAUTHORIZED, "Unauthorized", ex.getMessage(), req.getRequestURI());		
	}
	
	// ----------------------------
    //  JWT AUTH ERRORS
    // ----------------------------
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtError(JwtAuthenticationException ex, HttpServletRequest req) {
        return build(HttpStatus.UNAUTHORIZED, "Invalid or Expired Token", ex.getMessage(), req.getRequestURI());
    }
	
	// ----------------------------
    //  ACCESS DENIED
    // ----------------------------
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        return build(HttpStatus.FORBIDDEN, "Access Denied", "You don't have permission to access this resource", req.getRequestURI());
    }

    // ----------------------------
    //  VALIDATION ERRORS
    // ----------------------------
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");
        return build(HttpStatus.BAD_REQUEST, "Validation Failed", msg, req.getRequestURI());
    }


    // ----------------------------
    //  CONSTRAINT VIOLATIONS
    // ----------------------------
    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<ErrorResponse> handleConstraint(ConstraintViolationException ex, HttpServletRequest req) {
        String msg = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .findFirst()
                .orElse("Constraint violation");
        return build(HttpStatus.BAD_REQUEST, "Constraint Violation", msg, req.getRequestURI());
    }

    // ----------------------------
    //  DATA INTEGRITY ERRORS
    // ----------------------------
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "Data Integrity Violation", "Request violates data constraints", req.getRequestURI());
    }

    // ----------------------------
    //  FALLBACK GENERIC ERROR
    // ----------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), req.getRequestURI());
    }
    
    // ----------------------------
    //  HELPER METHOD
    // ----------------------------
    private ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message, String path) {
        return ResponseEntity.status(status).body(
                ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(status.value())
                        .error(error)
                        .message(message)
                        .path(path)
                        .build()
        );
    }
}
