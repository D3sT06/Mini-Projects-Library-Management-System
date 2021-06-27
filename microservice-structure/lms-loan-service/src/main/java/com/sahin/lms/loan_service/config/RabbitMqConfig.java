package com.sahin.lms.loan_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
@ConditionalOnExpression("${amqp.enabled}")
public class RabbitMqConfig {

    @Bean
    public Queue loanQueue() {
        return new Queue("loans", false);
    }

    @Bean
    public Queue reservationQueue() {
        return new Queue("reservations", false);
    }

    @Bean
    public TopicExchange memberEventsTopicExchange() {
        return new TopicExchange("member_events");
    }

    @Bean
    public Declarables topicBindings() {
        return new Declarables(
                binding(loanQueue(), memberEventsTopicExchange()),
                binding(reservationQueue(), memberEventsTopicExchange())
        );
    }

    private Binding binding(Queue queue, TopicExchange topicExchange) {
        return BindingBuilder.bind(queue)
                .to(topicExchange)
                .with(queue.getName());
    }

}
