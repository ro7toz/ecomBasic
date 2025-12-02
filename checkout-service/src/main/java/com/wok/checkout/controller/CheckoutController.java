package com.wok.checkout.controller;

import com.wok.checkout.dto.CheckoutRequest;
import com.wok.checkout.dto.CheckoutResponse;
import com.wok.checkout.service.CheckoutService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @PostMapping
    public ResponseEntity<CheckoutResponse> processCheckout(@RequestBody CheckoutRequest request) {
        try {
            var result = checkoutService.processCheckout(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (Exception e) {
            throw new RuntimeException("Checkout failed: " + e.getMessage());
        }
    }
}
