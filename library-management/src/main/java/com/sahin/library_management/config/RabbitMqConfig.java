package com.sahin.library_management.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMqConfig {

    @Bean
    public Queue bookQueue() {
        return new Queue("books", false);
    }

    @Bean
    public Queue itemQueue() {
        return new Queue("book_items", false);
    }

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
                binding(bookQueue(), memberEventsTopicExchange()),
                binding(itemQueue(), memberEventsTopicExchange()),
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
