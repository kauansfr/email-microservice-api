package com.azenithsolutions.emailmicroservice.infrastructure.config;

import java.util.Map;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

@Configuration
public class RabbitMQConfig {

    @Value("${app.messaging.email.exchange}")
    private String emailExchangeName;
    @Value("${app.messaging.email.queue}")
    private String emailQueueName;
    @Value("${app.messaging.email.routing-key}")
    private String emailRoutingKey;
    @Value("${app.messaging.email.dlx}")
    private String emailDlxName;
    @Value("${app.messaging.email.dlq}")
    private String emailDlqName;

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public DirectExchange emailExchange() {
        return ExchangeBuilder
                .directExchange(emailExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange emailDeadLetterExchange() {
        return ExchangeBuilder
                .directExchange(emailDlxName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(emailQueueName)
                .withArguments(Map.of(
                        "x-dead-letter-exchange", emailDlxName,
                        "x-dead-letter-routing-key", emailRoutingKey // (mesmo routing-key para simplicidade)
                ))
                .build();
    }

    @Bean
    public Queue emailDeadLetterQueue() {
        return QueueBuilder.durable(emailDlqName).build();
    }

    @Bean
    public Binding emailBinding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder
                .bind(emailQueue)
                .to(emailExchange)
                .with(emailRoutingKey);
    }

    @Bean
    public Binding emailDlqBinding(Queue emailDeadLetterQueue, DirectExchange emailDeadLetterExchange) {
        return BindingBuilder
                .bind(emailDeadLetterQueue)
                .to(emailDeadLetterExchange)
                .with(emailRoutingKey);
    }

    @Bean
    public MessageConverter jacksonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter);
        return template;
    }

    @Bean
    public RetryOperationsInterceptor emailRetryInterceptor(RabbitTemplate rabbitTemplate) {
        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(
                rabbitTemplate,
                emailDlxName,
                emailRoutingKey
        );

        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000) // initial 1s, multiplica por 2 - até 10s
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public RabbitListenerContainerFactory<?> emailListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter,
            RetryOperationsInterceptor emailRetryInterceptor) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jacksonMessageConverter);
        factory.setAdviceChain(emailRetryInterceptor);
        return factory;
    }
}
