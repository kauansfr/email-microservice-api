package com.azenithsolutions.emailmicroservice.infrastructure.config;

import com.azenithsolutions.emailmicroservice.application.email.usecases.SendEmailUseCase;
import com.azenithsolutions.emailmicroservice.domain.EmailGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class EmailUseCaseConfig {
    @Bean
    SendEmailUseCase sendEmailUseCase(EmailGateway gateway) { return new sendEmailUseCase(gateway); }
}
