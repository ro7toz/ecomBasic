package com.wok.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDTO(
        Long id,
        Long orderId,
        BigDecimal amount,
        String transactionId,
        String status,
        LocalDateTime createdAt
) {}
