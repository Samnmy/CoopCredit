package com.coopcredit.creditapplicationservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "com.coopcredit.creditapplicationservice.infrastructure.persistence.repository",
        entityManagerFactoryRef = "entityManagerFactory"
)
public class JpaConfig {
}