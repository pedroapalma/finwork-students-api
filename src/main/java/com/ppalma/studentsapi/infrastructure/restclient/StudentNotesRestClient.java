package com.ppalma.studentsapi.infrastructure.restclient;

import com.ppalma.studentsapi.application.exception.InternalServerException;
import com.ppalma.studentsapi.domain.model.Student;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class StudentNotesRestClient {

  private final RestClient restClient;

  @Value("${rest.endpoint.students-notes.send-msg-for-avg-notes}")
  private String sendMsgForAvgNotes;

  public StudentNotesRestClient(@Value("${rest.endpoint.students-notes.url-base}") String baseUrl,
      @Value("${rest.endpoint.students-notes.url-prefix}") String prefixUrl) {
    this.restClient = RestClient.builder()
        .baseUrl(String.format("%s%s", baseUrl, prefixUrl))
        .build();
  }

  public void sendMsgToTopicForAvgNotes(Student student) {
    this.restClient.post()
        .uri(this.sendMsgForAvgNotes)
        .body(student)
        .retrieve()
        .onStatus(HttpStatusCode::isError, (request, response) -> {
          throw new InternalServerException(
              String.format("Status: %s, Headers: %s", response.getStatusCode(),
                  response.getHeaders()));
        })
        .toBodilessEntity();
  }

}
