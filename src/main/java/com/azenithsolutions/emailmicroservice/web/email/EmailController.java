package com.azenithsolutions.emailmicroservice.web.email;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Email Microservice - v1", description = "Endpoints for sending emails with and without attachments")
@RestController
@RequestMapping("/emails")
public class EmailController {

}
