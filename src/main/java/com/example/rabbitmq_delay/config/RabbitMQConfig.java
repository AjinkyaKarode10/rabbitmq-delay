package com.example.rabbitmq_delay.config;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  // Queue and exchange names
  public static final String QUEUE_ONE = "queue_one";
  public static final String QUEUE_TWO = "queue_two";
  public static final String DLX_QUEUE = "queue_dlq";
  public static final String QUEUE_ONE_EXCHANGE = "queue_one_exchange";
  public static final String QUEUE_TWO_EXCHANGE = "queue_two_exchange";
  public static final String DLX_EXCHANGE = "queue_dlx_exchange";
  public static final String QUEUE_ONE_ROUTING_KEY = "queue_one.key";
  public static final String QUEUE_TWO_ROUTING_KEY = "queue_two.key";
  public static final String DLX_QUEUE_ROUTING_KEY = "max.failure";

  // Exchange for retry expiry queue
  @Bean
  public DirectExchange queueOneExchange() {
    return new DirectExchange(QUEUE_ONE_EXCHANGE, true, false);
  }

  @Bean
  public DirectExchange queueTwoExchange() {
    return new DirectExchange(QUEUE_TWO_EXCHANGE);
  }

  @Bean
  public DirectExchange dlxExchange() {
    return new DirectExchange(DLX_EXCHANGE);
  }

  @Bean
  public Queue retryQueue() {
    return QueueBuilder.durable(QUEUE_ONE)
        .deadLetterExchange(QUEUE_TWO_EXCHANGE)
        .deadLetterRoutingKey(QUEUE_TWO_ROUTING_KEY)
        .quorum()
        .build();  // Builds the queue
  }

  @Bean
  public Queue retryWorkQueue() {
    return QueueBuilder.durable(QUEUE_TWO)  // Make it durable
        .quorum()
        .build();
  }

  // Final DLX Queue for handling messages that exceeded max retries - Quorum Queue
  @Bean
  public Queue dlxQueue() {
    return QueueBuilder.durable(DLX_QUEUE)  // Make it durable
        .quorum()
        .build();
  }

  @Bean
  public Binding queueOneBinding() {
    return BindingBuilder.bind(retryQueue()).to(queueOneExchange()).with(QUEUE_ONE_ROUTING_KEY);
  }

  @Bean
  public Binding queueTwoBinding() {
    return BindingBuilder.bind(retryWorkQueue()).to(queueTwoExchange()).with(QUEUE_TWO_ROUTING_KEY);
  }

  @Bean
  public Binding dlxBinding() {
    return BindingBuilder.bind(dlxQueue()).to(dlxExchange()).with(DLX_QUEUE_ROUTING_KEY);
  }

  // Message Converter to convert messages to JSON
  @Bean
  public MessageConverter messageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  // RabbitTemplate bean to send messages to RabbitMQ
  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);  // Ensures message conversion to JSON
    return rabbitTemplate;
  }
}
