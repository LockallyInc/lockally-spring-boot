package com.lockally.spring;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration that exposes a {@link LockallyMailSender} bean whenever
 * {@code lockally.api-key} is set.
 */
@AutoConfiguration
@EnableConfigurationProperties(LockallyProperties.class)
@ConditionalOnProperty(prefix = "lockally", name = "api-key")
public class LockallyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public LockallyMailSender lockallyMailSender(LockallyProperties properties) {
        return LockallyMailSender.fromApiKey(properties.getApiKey(), properties.getBaseUrl());
    }
}
