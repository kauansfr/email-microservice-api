package com.azenithsolutions.emailmicroservice.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azenithsolutions.emailmicroservice.application.email.usecases.SendEmailUseCase;
import com.azenithsolutions.emailmicroservice.domain.EmailGateway;

@Configuration
public class EmailUseCaseConfig {
    @Bean
    public SendEmailUseCase sendEmailUseCase(EmailGateway gateway) {
        return new SendEmailUseCase(gateway);
    }
}