package com.ppalma.studentsapi.infrastructure.adapter;

import com.ppalma.studentsapi.application.exception.InternalServerException;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.domain.port.StudentProducerApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Slf4j
@Component
public class StudentProducerRestClient implements StudentProducerApi {

  private final RestClient restClient;

  @Value("${rest.endpoint.students-notes.send-msg-for-avg-notes}")
  private String sendMsgForAvgNotes;

  public StudentProducerRestClient(RestClient restClient) {
    this.restClient = restClient;
  }

  @Override
  public void saveAsyncStudentWithAvgNotes(Student student) {
    this.restClient.post()
        .uri(this.sendMsgForAvgNotes)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
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
