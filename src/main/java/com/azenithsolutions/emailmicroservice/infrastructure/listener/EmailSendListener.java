package com.azenithsolutions.emailmicroservice.infrastructure.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.azenithsolutions.emailmicroservice.application.email.usecases.SendEmailUseCase;
import com.azenithsolutions.emailmicroservice.domain.EmailBudget;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSendListener {

    private final SendEmailUseCase sendEmailUseCase;

    @RabbitListener(queues = "${app.messaging.email.queue}", containerFactory = "emailListenerContainerFactory")
    public void onMessage(EmailBudget budget) {
        log.info("Mensagem recebida para envio de email: to={} subject={}", budget.getToEmail(), budget.getSubject());
        try {
            sendEmailUseCase.execute(budget);
            log.info("Email processado com sucesso para {}", budget.getToEmail());
        } catch (Exception exception) {
            log.error("Erro ao processar email para {}: {}", budget.getToEmail(), exception.getMessage(), exception);
            throw exception; // acionar retry
        }
    }
}
