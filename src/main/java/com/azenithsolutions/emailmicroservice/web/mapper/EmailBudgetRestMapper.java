package com.azenithsolutions.emailmicroservice.web.mapper;

import com.azenithsolutions.emailmicroservice.domain.EmailBudget;
import com.azenithsolutions.emailmicroservice.web.rest.EmailBudgetRest;

public class EmailBudgetRestMapper {
    public static EmailBudgetRest toRest(EmailBudget domain) {
        EmailBudgetRest rest = new EmailBudgetRest();
        rest.setToEmail(domain.getToEmail());
        rest.setToName(domain.getToName());
        rest.setSubject(domain.getSubject());
        rest.setContent(domain.getContent());

        return rest;
    }

    public static EmailBudget toDomain(EmailBudgetRest rest) {
        EmailBudget domain = new EmailBudget();
        domain.setToEmail(rest.getToEmail());
        domain.setToName(rest.getToName());
        domain.setSubject(rest.getSubject());
        domain.setContent(rest.getContent());

        return domain;
    }
}
