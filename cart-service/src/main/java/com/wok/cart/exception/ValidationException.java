package com.wok.cart.exception;  // Change catalog to service name

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
