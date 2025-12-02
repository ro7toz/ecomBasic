package com.wok.order.service;

import com.wok.order.dto.OrderDTO;
import com.wok.order.entity.Order;
import com.wok.order.entity.OrderItem;
import com.wok.order.repository.OrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderDTO createOrder(Long userId, List<OrderItem> items, BigDecimal totalAmount) {
        var order = new Order();
        order.setUserId(userId);
        order.setOrderNumber("ORD-" + UUID.randomUUID().toString()
                .substring(0, 8).toUpperCase());
        order.setItems(items);
        order.setTotalAmount(totalAmount);
        order.setStatus("PENDING");

        var saved = orderRepository.save(order);
        return toDTO(saved);
    }

    public OrderDTO getOrder(Long orderId) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        return toDTO(order);
    }

    public Page<OrderDTO> getUserOrders(Long userId, int page, int size) {
        return orderRepository.findByUserId(userId, PageRequest.of(page, size))
                .map(this::toDTO);
    }

    public OrderDTO updateOrderStatus(Long orderId, String status) {
        var order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
        order.setStatus(status);
        var updated = orderRepository.save(order);
        return toDTO(updated);
    }

    private OrderDTO toDTO(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getUserId(),
                order.getOrderNumber(),
                order.getItems(),
                order.getTotalAmount(),
                order.getStatus()
        );
    }
}
