package com.example.rabbitmq_delay.service;

import java.time.Instant;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rabbitmq_delay.config.RabbitMQConfig;
import com.example.rabbitmq_delay.dto.OnboardUserRequestDTO;

@Service
public class OnboardUserService {

  @Autowired
  RabbitTemplate rabbitTemplate;

  private static final Logger log = LoggerFactory.getLogger(OnboardUserService.class);

  public void onboardUser(OnboardUserRequestDTO onboardUserRequestDTO) {
    MessagePostProcessor messagePostProcessor = message -> {
      message.getMessageProperties().setExpiration(String.valueOf(20000));//20 sec
      return message;
    };
    rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_ONE, onboardUserRequestDTO,messagePostProcessor);
    log.info("User Onboard request sent to queue : {} - {}", onboardUserRequestDTO.getUserId(), LocalDateTime.now().toString().substring(0, 19));
  }
}
