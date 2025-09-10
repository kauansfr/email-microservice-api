package com.azenithsolutions.emailmicroservice.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${brevo.key}") private String brevoKey;
    @Value("${brevo.url}") private String brevoUrl;

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean(name = "brevoWebClient")
    public WebClient brevoWebClient() {
        return WebClient.builder()
                .defaultHeader("api-key", brevoKey)
                .defaultHeader("Content-Type", "application/json")
                .baseUrl(brevoUrl)
                .build();
    }
}
