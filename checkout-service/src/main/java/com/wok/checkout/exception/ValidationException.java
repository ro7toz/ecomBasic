package com.wok.checkout.exception;  // Change catalog to service name

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
