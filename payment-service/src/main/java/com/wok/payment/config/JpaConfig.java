package com.wok.payment.config;  // Change catalog to service name

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.ecommerce.wok.catalog.repository")  // Change
public class JpaConfig {
}
