package com.wok.order.config;  // Change catalog to service name

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wok.order.repository")  // Change
public class JpaConfig {
}
