package com.wok.order.dto;

import com.wok.order.entity.OrderItem;
import java.math.BigDecimal;
import java.util.List;

public record CreateOrderRequest(
        Long userId,
        List<OrderItem> items,
        BigDecimal totalAmount
) {}