package com.wok.subscription.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_status", columnList = "status")
})
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan plan;

    @Column(length = 50)
    private String status;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "renewal_date")
    private LocalDateTime renewalDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public SubscriptionPlan getPlan() { return plan; }
    public void setPlan(SubscriptionPlan plan) { this.plan = plan; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public LocalDateTime getRenewalDate() { return renewalDate; }
    public void setRenewalDate(LocalDateTime renewalDate) { this.renewalDate = renewalDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
