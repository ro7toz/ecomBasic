package com.wok.subscription.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SubscriptionDTO(
        Long id,
        Long userId,
        String planName,
        BigDecimal monthlyPrice,
        String status,
        LocalDateTime startDate,
        LocalDateTime renewalDate
) {}
