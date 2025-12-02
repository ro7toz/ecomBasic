package com.wok.checkout.exception;  // Change catalog to service name

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
