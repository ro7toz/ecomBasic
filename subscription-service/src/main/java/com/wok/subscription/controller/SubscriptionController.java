package com.wok.subscription.controller;

import com.wok.subscription.dto.SubscriptionDTO;
import com.wok.subscription.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<SubscriptionDTO> getActiveSubscription(@PathVariable Long userId) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(userId));
    }

    @DeleteMapping("/{subscriptionId}")
    public ResponseEntity<Void> cancelSubscription(@PathVariable Long subscriptionId) {
        subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.noContent().build();
    }
}
