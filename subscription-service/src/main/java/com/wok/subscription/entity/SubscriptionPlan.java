package com.wok.subscription.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "subscription_plans")
public class SubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100)
    private String planName;

    @Column(precision = 10, scale = 2)
    private BigDecimal monthlyPrice;

    private int billingCycleDays;

    @Column(length = 500)
    private String description;

    private boolean active;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPlanName() { return planName; }
    public void setPlanName(String planName) { this.planName = planName; }
    public BigDecimal getMonthlyPrice() { return monthlyPrice; }
    public void setMonthlyPrice(BigDecimal monthlyPrice) { this.monthlyPrice = monthlyPrice; }
    public int getBillingCycleDays() { return billingCycleDays; }
    public void setBillingCycleDays(int billingCycleDays) { this.billingCycleDays = billingCycleDays; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
