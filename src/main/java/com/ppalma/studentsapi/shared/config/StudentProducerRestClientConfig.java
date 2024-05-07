package com.ppalma.studentsapi.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class StudentProducerRestClientConfig {

  @Value("${rest.endpoint.students-notes.url-base}")
  private String baseUrl;
  @Value("${rest.endpoint.students-notes.url-prefix}")
  private String prefixUrl;

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .baseUrl(String.format("%s%s", this.baseUrl, this.prefixUrl))
        .build();
  }

}
