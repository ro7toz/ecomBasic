package com.wok.order.entity;

public sealed interface OrderStatus permits PendingStatus, ConfirmedStatus,
        ShippedStatus, DeliveredStatus,
        CancelledStatus {
    String value();
}

record PendingStatus() implements OrderStatus {
    @Override
    public String value() { return "PENDING"; }
}

record ConfirmedStatus() implements OrderStatus {
    @Override
    public String value() { return "CONFIRMED"; }
}

record ShippedStatus() implements OrderStatus {
    @Override
    public String value() { return "SHIPPED"; }
}

record DeliveredStatus() implements OrderStatus {
    @Override
    public String value() { return "DELIVERED"; }
}

record CancelledStatus() implements OrderStatus {
    @Override
    public String value() { return "CANCELLED"; }
}
