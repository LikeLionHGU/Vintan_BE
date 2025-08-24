package com.vintan.config.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Application-wide configuration class.
 * Defines beans that can be used across the application context.
 */
@Configuration
public class AppConfig {

    /**
     * Creates and registers a RestTemplate bean.
     * RestTemplate is used to make HTTP requests to external APIs.
     *
     * @return a new instance of RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
