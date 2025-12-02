package com.wok.cart.exception;  // Change catalog to service name

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {
        return buildErrorResponse("Resource not found", ex.getMessage(), 404);
    }

    @ExceptionHandler(InsufficientStockException.class)
    public ResponseEntity<Map<String, Object>> handleInsufficientStock(
            InsufficientStockException ex) {
        return buildErrorResponse("Insufficient stock", ex.getMessage(), 409);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            ValidationException ex) {
        return buildErrorResponse("Validation error", ex.getMessage(), 400);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildErrorResponse("Internal server error", ex.getMessage(), 500);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(
            String error, String message, int status) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", error);
        response.put("message", message);
        response.put("status", status);
        response.put("timestamp", LocalDateTime.now());
        return ResponseEntity.status(status).body(response);
    }
}
