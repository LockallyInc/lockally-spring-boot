package com.lockally.spring;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for the Lockally Spring Boot starter, bound from {@code lockally.*}.
 *
 * <pre>
 * lockally.api-key=lk_live_...   # lk_test_... runs in sandbox
 * lockally.base-url=https://api.lockally.com   # optional override
 * </pre>
 */
@ConfigurationProperties(prefix = "lockally")
public class LockallyProperties {

    /** Lockally API key. {@code lk_test_} runs in sandbox, {@code lk_live_} sends real mail. */
    private String apiKey;

    /** Optional base URL override (e.g. a private endpoint). */
    private String baseUrl;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }
}
