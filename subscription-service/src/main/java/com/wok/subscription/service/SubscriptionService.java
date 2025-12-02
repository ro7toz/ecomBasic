package com.wok.subscription.service;

import com.wok.subscription.dto.SubscriptionDTO;
import com.wok.subscription.entity.Subscription;
import com.wok.subscription.entity.SubscriptionPlan;
import com.wok.subscription.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@Transactional
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final RestTemplate restTemplate;

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
                               RestTemplate restTemplate) {
        this.subscriptionRepository = subscriptionRepository;
        this.restTemplate = restTemplate;
    }

    public SubscriptionDTO createSubscription(Long userId, SubscriptionPlan plan) {
        var subscription = new Subscription();
        subscription.setUserId(userId);
        subscription.setPlan(plan);
        subscription.setStatus("ACTIVE");
        subscription.setStartDate(LocalDateTime.now());
        subscription.setRenewalDate(LocalDateTime.now()
                .plusDays(plan.getBillingCycleDays()));

        var saved = subscriptionRepository.save(subscription);
        return toDTO(saved);
    }

    public SubscriptionDTO getActiveSubscription(Long userId) {
        var subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, "ACTIVE")
                .orElseThrow(() -> new IllegalArgumentException("No active subscription found"));
        return toDTO(subscription);
    }

    public void cancelSubscription(Long subscriptionId) {
        var subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));
        subscription.setStatus("CANCELLED");
        subscriptionRepository.save(subscription);
    }

    @Scheduled(fixedDelay = 86400000) // Run daily
    public void processRenewals() {
        var now = LocalDateTime.now();
        var nextDay = now.plusDays(1);

        var dueSubs = subscriptionRepository
                .findByRenewalDateBetweenAndStatus(now, nextDay, "ACTIVE");

        // Virtual threads handle this naturally
        dueSubs.forEach(sub -> {
            try {
                processSubscriptionRenewal(sub);
            } catch (Exception e) {
                sub.setStatus("SUSPENDED");
                subscriptionRepository.save(sub);
            }
        });
    }

    private void processSubscriptionRenewal(Subscription subscription) {
        var paymentPayload = Map.of(
                "userId", subscription.getUserId(),
                "amount", subscription.getPlan().getMonthlyPrice(),
                "paymentMethod", "AUTO_RENEWAL"
        );

        var paymentUrl = "http://localhost:8005/api/payments";
        var payment = restTemplate.postForObject(paymentUrl, paymentPayload, Map.class);

        if (payment instanceof Map<?, ?> payMap &&
                "COMPLETED".equals(payMap.get("status"))) {
            subscription.setRenewalDate(
                    subscription.getRenewalDate()
                            .plusDays(subscription.getPlan().getBillingCycleDays())
            );
            subscriptionRepository.save(subscription);
        }
    }

    private SubscriptionDTO toDTO(Subscription s) {
        return new SubscriptionDTO(
                s.getId(),
                s.getUserId(),
                s.getPlan().getPlanName(),
                s.getPlan().getMonthlyPrice(),
                s.getStatus(),
                s.getStartDate(),
                s.getRenewalDate()
        );
    }
}
