package com.ppalma.studentsapi.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ppalma.studentsapi.application.exception.InternalServerException;
import com.ppalma.studentsapi.domain.port.StudentProducerApi;
import com.ppalma.studentsapi.fake.StudentFaker;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.function.Consumer;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
class StudentProducerRestClientTest {

  public static final String STUDENTS_MESSAGES_AVERAGE_NOTES_URI = "/api/v1/students/messages/average-notes";
  private MockWebServer server;
  private StudentProducerApi studentProducerRestClient;

  @BeforeEach
  void setUp() {
    this.server = new MockWebServer();
    RestClient restClient = RestClient
        .builder()
        .requestFactory(new org.springframework.http.client.SimpleClientHttpRequestFactory())
        .baseUrl(this.server.url("/").toString())
        .build();
    this.studentProducerRestClient = new StudentProducerRestClient(restClient);
  }

  @AfterEach
  void shutdown() throws IOException {
    if (this.server != null) {
      this.server.shutdown();
    }
  }

  @Test
  void saveAsyncStudentWithAvgNotesSuccessfully() {
    ReflectionTestUtils.setField(this.studentProducerRestClient, "sendMsgForAvgNotes",
        STUDENTS_MESSAGES_AVERAGE_NOTES_URI);
    this.prepareResponse(response -> response.setResponseCode(HttpStatus.OK.value())
        .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
        .setBody(StringUtils.EMPTY));

    this.studentProducerRestClient.saveAsyncStudentWithAvgNotes(StudentFaker.getStudent());

    this.expectRequestCount(1);
    this.expectRequest(request -> {
      assertThat(request.getPath()).isEqualTo(STUDENTS_MESSAGES_AVERAGE_NOTES_URI);
      assertThat(request.getBody().readUtf8()).isEqualTo(StudentFaker.getJsonStudent());
      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo("application/json");
      assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo("application/json");
    });
  }

  @Test
  void returnInternalServerExceptionWhenApiFails() {
    ReflectionTestUtils.setField(this.studentProducerRestClient, "sendMsgForAvgNotes",
        STUDENTS_MESSAGES_AVERAGE_NOTES_URI);
    this.prepareResponse(
        response -> response.setResponseCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
            .setBody(StringUtils.EMPTY));

    assertThatThrownBy(() -> this.studentProducerRestClient.saveAsyncStudentWithAvgNotes(
        StudentFaker.getStudent())).isInstanceOf(
        InternalServerException.class);

    this.expectRequestCount(1);
    this.expectRequest(request -> {
      assertThat(request.getPath()).isEqualTo(STUDENTS_MESSAGES_AVERAGE_NOTES_URI);
      assertThat(request.getBody().readUtf8()).isEqualTo(StudentFaker.getJsonStudent());
      assertThat(request.getHeader(HttpHeaders.ACCEPT)).isEqualTo("application/json");
      assertThat(request.getHeader(HttpHeaders.CONTENT_TYPE)).isEqualTo("application/json");
    });
  }

  private void prepareResponse(Consumer<MockResponse> consumer) {
    MockResponse response = new MockResponse();
    consumer.accept(response);
    this.server.enqueue(response);
  }

  private void expectRequest(Consumer<RecordedRequest> consumer) {
    try {
      consumer.accept(this.server.takeRequest());
    } catch (InterruptedException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private void expectRequestCount(int count) {
    assertThat(this.server.getRequestCount()).isEqualTo(count);
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.METHOD)
  @ParameterizedTest(name = "[{index}] {0}")
  @MethodSource("clientHttpRequestFactories")
  @interface ParameterizedRestClientTest {

  }

}