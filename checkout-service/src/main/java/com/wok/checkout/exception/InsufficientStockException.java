package com.wok.checkout.exception;  // Change catalog to service name

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
