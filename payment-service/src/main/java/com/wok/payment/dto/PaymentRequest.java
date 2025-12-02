package com.wok.payment.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        Long userId,
        BigDecimal amount,
        String paymentMethod
) {}
