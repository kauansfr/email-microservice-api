package com.azenithsolutions.emailmicroservice.web.rest;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmailBudgetRest {
    @NotBlank
    private String toEmail;

    @NotBlank
    private String toName;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}