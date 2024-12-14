package com.example.rabbitmq_delay.dto;

import lombok.Data;

@Data
public class OnboardUserRequestDTO {

  private String userId;
  private String userName;
  private String email;
}
