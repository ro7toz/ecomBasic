package com.wok.cart.entity;

import java.math.BigDecimal;
import java.io.Serializable;

public record CartItem(
        Long productId,
        String productName,
        int quantity,
        BigDecimal price,
        BigDecimal subtotal
) implements Serializable {

    public CartItem {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }

    public CartItem withQuantity(int newQuantity) {
        var newSubtotal = price.multiply(BigDecimal.valueOf(newQuantity));
        return new CartItem(productId, productName, newQuantity, price, newSubtotal);
    }
}
