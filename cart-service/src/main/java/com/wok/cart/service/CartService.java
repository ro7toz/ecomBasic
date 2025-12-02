package com.wok.cart.service;

import com.wok.cart.entity.Cart;
import com.wok.cart.entity.CartItem;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RestTemplate restTemplate;
    private static final String CART_KEY_PREFIX = "cart:";
    private static final long CART_EXPIRY_DAYS = 30;

    public CartService(RedisTemplate<String, Object> redisTemplate,
                       RestTemplate restTemplate) {
        this.redisTemplate = redisTemplate;
        this.restTemplate = restTemplate;
    }

    public Cart getCart(Long userId) {
        var key = CART_KEY_PREFIX + userId;
        var cachedCart = (Cart) redisTemplate.opsForValue().get(key);
        return cachedCart != null ? cachedCart : new Cart(userId);
    }

    public Cart addToCart(Long userId, Long productId, int quantity) {
        // Fetch product from Catalog Service
        var productUrl = "http://localhost:8001/api/products/" + productId;

        // Using restTemplate with virtual threads - blocks naturally
        try {
            var response = restTemplate.getForObject(productUrl, Map.class);

            if (response == null) {
                throw new IllegalArgumentException("Product not found");
            }

            // Pattern matching for type safety (Java 21)
            if (response instanceof Map<?, ?> map) {
                var price = new BigDecimal(map.get("price").toString());
                var productName = (String) map.get("name");

                var cart = getCart(userId);
                var item = new CartItem(productId, productName, quantity, price,
                        price.multiply(BigDecimal.valueOf(quantity)));

                var updatedCart = cart.addItem(item);
                saveCart(userId, updatedCart);
                return updatedCart;
            }

            throw new IllegalStateException("Invalid product response format");
        } catch (Exception e) {
            throw new RuntimeException("Failed to add item to cart: " + e.getMessage());
        }
    }

    public Cart removeFromCart(Long userId, Long productId) {
        var cart = getCart(userId);
        var updated = cart.removeItem(productId);
        saveCart(userId, updated);
        return updated;
    }

    public Cart updateCartItem(Long userId, Long productId, int quantity) {
        var cart = getCart(userId);
        var updated = cart.updateQuantity(productId, quantity);
        saveCart(userId, updated);
        return updated;
    }

    public Cart clearCart(Long userId) {
        var cart = new Cart(userId);
        saveCart(userId, cart);
        return cart;
    }

    private void saveCart(Long userId, Cart cart) {
        var key = CART_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, cart, CART_EXPIRY_DAYS, TimeUnit.DAYS);
    }
}
