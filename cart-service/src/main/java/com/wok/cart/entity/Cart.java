package com.wok.cart.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

public record Cart(
        Long userId,
        List<CartItem> items,
        BigDecimal total,
        long createdAt
) implements Serializable {

    public Cart(Long userId) {
        this(userId, new ArrayList<>(), BigDecimal.ZERO, System.currentTimeMillis());
    }

    public Cart addItem(CartItem newItem) {
        var updatedItems = new ArrayList<>(items);

        var existing = updatedItems.stream()
                .filter(item -> item.productId().equals(newItem.productId()))
                .findFirst();

        if (existing.isPresent()) {
            int index = updatedItems.indexOf(existing.get());
            var updated = existing.get()
                    .withQuantity(existing.get().quantity() + newItem.quantity());
            updatedItems.set(index, updated);
        } else {
            updatedItems.add(newItem);
        }

        return calculateTotal(new Cart(userId, updatedItems, BigDecimal.ZERO, createdAt));
    }

    public Cart removeItem(Long productId) {
        var filtered = items.stream()
                .filter(item -> !item.productId().equals(productId))
                .toList();
        return calculateTotal(new Cart(userId, new ArrayList<>(filtered), BigDecimal.ZERO, createdAt));
    }

    public Cart updateQuantity(Long productId, int quantity) {
        var updated = new ArrayList<>(items);
        var item = updated.stream()
                .filter(i -> i.productId().equals(productId))
                .findFirst();

        if (item.isPresent()) {
            int index = updated.indexOf(item.get());
            updated.set(index, item.get().withQuantity(quantity));
        }

        return calculateTotal(new Cart(userId, updated, BigDecimal.ZERO, createdAt));
    }

    public Cart clear() {
        return new Cart(userId);
    }

    private static Cart calculateTotal(Cart cart) {
        var sum = cart.items.stream()
                .map(CartItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new Cart(cart.userId, cart.items, sum, cart.createdAt);
    }

    public int itemCount() {
        return items.size();
    }
}
