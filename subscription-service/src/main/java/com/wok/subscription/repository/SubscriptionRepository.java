package com.wok.subscription.repository;

import com.wok.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByUserIdAndStatus(Long userId, String status);
    List<Subscription> findByRenewalDateBetweenAndStatus(LocalDateTime start, LocalDateTime end, String status);
}
