package com.azenithsolutions.emailmicroservice;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class EmailMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmailMicroserviceApplication.class, args);
    }

}
