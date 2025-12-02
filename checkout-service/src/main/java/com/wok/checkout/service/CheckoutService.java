package com.wok.checkout.service;

import com.wok.checkout.dto.CheckoutRequest;
import com.wok.checkout.dto.CheckoutResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CheckoutService {

    private final RestTemplate restTemplate;

    public CheckoutService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CheckoutResponse processCheckout(CheckoutRequest request) {
        Long userId = request.userId();
        String cartUrl = "http://localhost:8002/api/cart/" + userId;

        // Fetch cart
        var cart = restTemplate.getForObject(cartUrl, Map.class);

        if (cart == null) {
            throw new IllegalStateException("Cart not found");
        }

        // Pattern matching and type casting (Java 21)
        if (!(cart instanceof Map<?, ?> cartMap)) {
            throw new IllegalStateException("Invalid cart format");
        }

        // Validate inventory
        List<Map> items = (List<Map>) cartMap.get("items");

        for (Map item : items) {
            Long productId = Long.parseLong(item.get("productId").toString());
            int quantity = (int) item.get("quantity");

            var productUrl = "http://localhost:8001/api/products/" + productId;
            var product = restTemplate.getForObject(productUrl, Map.class);

            if (product instanceof Map<?, ?> prodMap) {
                int stock = (int) prodMap.get("stock");
                if (stock < quantity) {
                    throw new IllegalStateException("Insufficient stock for product: " + productId);
                }
            }
        }

        // Create order
        var orderPayload = Map.of(
                "userId", userId,
                "items", items,
                "totalAmount", cartMap.get("total")
        );

        var orderUrl = "http://localhost:8003/api/orders";
        var order = restTemplate.postForObject(orderUrl, orderPayload, Map.class);

        if (order == null) {
            throw new IllegalStateException("Failed to create order");
        }

        // Process payment
        var paymentPayload = Map.of(
                "orderId", order.get("id"),
                "userId", userId,
                "amount", order.get("totalAmount"),
                "paymentMethod", request.paymentMethod()
        );

        var paymentUrl = "http://localhost:8005/api/payments";
        var payment = restTemplate.postForObject(paymentUrl, paymentPayload, Map.class);

        if (payment instanceof Map<?, ?> payMap &&
                !"COMPLETED".equals(payMap.get("status"))) {
            throw new RuntimeException("Payment failed");
        }

        // Clear cart
        var clearUrl = "http://localhost:8002/api/cart/" + userId + "/clear";
        restTemplate.delete(clearUrl);

        // Return success
        return new CheckoutResponse(
                (Long) order.get("id"),
                (String) order.get("orderNumber"),
                (BigDecimal) order.get("totalAmount"),
                "CHECKOUT_COMPLETED"
        );
    }
}
