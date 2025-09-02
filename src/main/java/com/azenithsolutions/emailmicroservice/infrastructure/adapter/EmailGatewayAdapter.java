package com.azenithsolutions.emailmicroservice.infrastructure.adapter;

import com.azenithsolutions.emailmicroservice.domain.EmailBudget;
import com.azenithsolutions.emailmicroservice.domain.EmailGateway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailGatewayAdapter implements EmailGateway {
    private final WebClient brevoClient;

    public EmailGatewayAdapter(@Qualifier("brevoWebClient") WebClient brevoClient) {
        this.brevoClient = brevoClient;
    }

    @Override
    public boolean hasEmailBeenSent(EmailBudget budget) {
        Map<String, Object> payload = buildPayload(budget);
        try {
            brevoClient.post()
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        }  catch (Exception e) {
            return false;
        }
    }

    private Map<String, Object> buildPayload(EmailBudget budget) {
        Map<String, Object> payload = new HashMap<>();

        payload.put("sender", Map.of(
            "name", "HardwareTech",
                "email", "hardwaretech@hardwaretech.com.br"
        ));
        payload.put("to", List.of(Map.of(
            "email", budget.getToEmail(),
                "name",budget.getToName()
        )));
        payload.put("subject", budget.getSubject());
        payload.put("content", budget.getContent());

        return payload;
    }
}
