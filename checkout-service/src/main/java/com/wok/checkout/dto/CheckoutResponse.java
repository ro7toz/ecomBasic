package com.wok.checkout.dto;

import java.math.BigDecimal;

public record CheckoutResponse(
        Long orderId,
        String orderNumber,
        BigDecimal totalAmount,
        String status
) {}
