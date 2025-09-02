package com.azenithsolutions.emailmicroservice.application.email.usecases;

import com.azenithsolutions.emailmicroservice.domain.EmailBudget;
import com.azenithsolutions.emailmicroservice.domain.EmailGateway;

public class SendEmailUseCase {
    private final EmailGateway gateway;

    public SendEmailUseCase(EmailGateway gateway) {
        this.gateway = gateway;
    }

    public String execute(EmailBudget budget) {
        boolean hasEmailBeenSent = gateway.hasEmailBeenSent(budget);

        if (!hasEmailBeenSent) throw new IllegalStateException("Falha ao enviar email!");

        return "Email enviado com sucesso!";
    }
}
