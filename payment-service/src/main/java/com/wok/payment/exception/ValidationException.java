package com.wok.payment.exception;  // Change catalog to service name

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
