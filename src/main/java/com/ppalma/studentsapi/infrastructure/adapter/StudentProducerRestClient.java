package com.ppalma.studentsapi.infrastructure.adapter;

import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.domain.port.StudentProducerApi;
import com.ppalma.studentsapi.infrastructure.restclient.StudentNotesRestClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudentProducerRestClient implements StudentProducerApi {

  private final StudentNotesRestClient studentNotesRestClient;

  @Override
  public void saveAsyncStudentWithAvgNotes(Student student) {
    this.studentNotesRestClient.sendMsgToTopicForAvgNotes(student);
  }
}
