package com.wok.order.dto;

import com.wok.order.entity.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public record OrderDTO(
        Long id,
        Long userId,
        String orderNumber,
        List<OrderItem> items,
        BigDecimal totalAmount,
        String status
) {}
