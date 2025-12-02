package com.wok.payment.service;

import com.wok.payment.dto.PaymentDTO;
import com.wok.payment.dto.PaymentRequest;
import com.wok.payment.entity.Payment;
import com.wok.payment.entity.PaymentStatus;
import com.wok.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentDTO processPayment(PaymentRequest request) {
        // Mock payment gateway integration
        var success = mockPaymentGateway();

        var payment = new Payment();
        payment.setOrderId(request.orderId());
        payment.setUserId(request.userId());
        payment.setAmount(request.amount());
        payment.setPaymentMethod(request.paymentMethod());
        payment.setTransactionId("TXN-" + UUID.randomUUID()
                .toString().substring(0, 12).toUpperCase());
        payment.setStatus(success ? PaymentStatus.COMPLETED : PaymentStatus.FAILED);

        var saved = paymentRepository.save(payment);
        return toDTO(saved);
    }

    public PaymentDTO getPaymentByOrderId(Long orderId) {
        var payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found for order: " + orderId));
        return toDTO(payment);
    }

    private boolean mockPaymentGateway() {
        // 95% success rate
        return Math.random() > 0.05;
    }

    private PaymentDTO toDTO(Payment p) {
        return new PaymentDTO(
                p.getId(),
                p.getOrderId(),
                p.getAmount(),
                p.getTransactionId(),
                p.getStatus().toString(),
                p.getCreatedAt()
        );
    }
}
