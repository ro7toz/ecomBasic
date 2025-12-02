package com.wok.cart.controller;

import com.wok.cart.entity.Cart;
import com.wok.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<Cart> addToCart(@PathVariable Long userId,
                                          @RequestParam Long productId,
                                          @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.addToCart(userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable Long userId,
                                               @PathVariable Long productId) {
        return ResponseEntity.ok(cartService.removeFromCart(userId, productId));
    }

    @PutMapping("/{userId}/items/{productId}")
    public ResponseEntity<Cart> updateCartItem(@PathVariable Long userId,
                                               @PathVariable Long productId,
                                               @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateCartItem(userId, productId, quantity));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
