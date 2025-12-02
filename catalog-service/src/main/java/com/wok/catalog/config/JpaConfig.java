package com.wok.catalog.config;  // Change catalog to service name

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.wok.catalog.repository")  // Change
public class JpaConfig {
}
