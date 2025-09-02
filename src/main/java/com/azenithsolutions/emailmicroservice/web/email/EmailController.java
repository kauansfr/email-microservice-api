package com.azenithsolutions.emailmicroservice.web.email;

import com.azenithsolutions.emailmicroservice.application.email.usecases.SendEmailUseCase;
import com.azenithsolutions.emailmicroservice.domain.EmailBudget;
import com.azenithsolutions.emailmicroservice.web.mapper.EmailBudgetRestMapper;
import com.azenithsolutions.emailmicroservice.web.rest.EmailBudgetRest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email Microservice - v1", description = "Endpoints for sending emails with and without attachments")
@RestController
@RequestMapping("/emails")
@AllArgsConstructor
public class EmailController {
    private final SendEmailUseCase send;

    public String sendEmail(@RequestBody EmailBudgetRest rest) {
        EmailBudget domain = EmailBudgetRestMapper.toDomain(rest);
        return send.execute(domain);
    }
}
