package com.example.rabbitmq_delay.service;

import java.time.Instant;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.example.rabbitmq_delay.config.RabbitMQConfig;
import com.example.rabbitmq_delay.dto.OnboardUserRequestDTO;

@Service
public class RabbitMQListener {

  private static final Logger log = LoggerFactory.getLogger(RabbitMQListener.class);

  @RabbitListener(queues = RabbitMQConfig.QUEUE_TWO)
  public void processRecord(OnboardUserRequestDTO onboardUserRequestDTO) {
    log.info("Received RabbitMQ message {} - {} ",onboardUserRequestDTO.getUserId(), LocalDateTime.now().toString().substring(0, 19));
  }
}
