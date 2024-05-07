package com.ppalma.studentsapi.infrastructure.adapter;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.ppalma.studentsapi.application.usescases.StudentUsesCases;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.fake.StudentFaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

@ExtendWith(MockitoExtension.class)
public class StudentKafkaConsumerTest {

  @Mock
  private StudentUsesCases studentUsesCases;

  @Mock
  private Acknowledgment acknowledgment;

  private StudentKafkaConsumer studentKafkaConsumer;

  @BeforeEach
  public void setUp() {
    this.studentKafkaConsumer = new StudentKafkaConsumer(this.studentUsesCases);
  }

  @Test
  public void saveStudentWithAvgNotesSuccessfully() {
    Student student = StudentFaker.getStudent();
    Student studentWithAvgNotes = StudentFaker.getStudentWithAvgNotes();

    this.studentKafkaConsumer.consume(student, 0, 1, this.acknowledgment);

    verify(this.studentUsesCases, times(1)).saveStudent(studentWithAvgNotes);
    verify(this.acknowledgment, times(1)).acknowledge();
  }
}
