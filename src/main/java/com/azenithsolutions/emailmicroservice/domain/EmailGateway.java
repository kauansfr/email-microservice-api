package com.azenithsolutions.emailmicroservice.domain;

public interface EmailGateway {
    boolean hasEmailBeenSent(EmailBudget budget);
}