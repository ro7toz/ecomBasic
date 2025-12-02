package com.wok.payment.controller;

import com.wok.payment.dto.PaymentDTO;
import com.wok.payment.dto.PaymentRequest;
import com.wok.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> processPayment(@RequestBody PaymentRequest request) {
        var payment = paymentService.processPayment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(payment);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentDTO> getPaymentByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.getPaymentByOrderId(orderId));
    }
}
