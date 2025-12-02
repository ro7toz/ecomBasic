package com.wok.checkout.dto;

import java.util.Map;

public record CheckoutRequest(
        Long userId,
        Map<String, String> shippingAddress,
        String paymentMethod
) {}
