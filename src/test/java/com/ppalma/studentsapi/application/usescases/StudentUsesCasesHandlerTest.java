package com.ppalma.studentsapi.application.usescases;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ppalma.studentsapi.application.exception.NotFoundException;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.domain.port.StudentKvs;
import com.ppalma.studentsapi.domain.port.StudentProducerApi;
import com.ppalma.studentsapi.fake.StudentFaker;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentUsesCasesHandlerTest {

  private static final String ID = "123456";

  private StudentUsesCases studentUsesCases;

  @Mock
  private StudentKvs studentKvs;

  @Mock
  private StudentProducerApi studentProducerApi;

  @Captor
  private ArgumentCaptor<Student> studentArgCaptor;

  @BeforeEach
  void setUp() {
    this.studentUsesCases = new StudentUsesCasesHandler(this.studentKvs, this.studentProducerApi);
  }

  @Test
  void saveStudentSuccessfully() {
    Student student = StudentFaker.getStudent();

    this.studentUsesCases.saveStudent(student);

    verify(this.studentKvs).save(student);
  }

  @Test
  void returnStudentWhenFindStudentById() {
    Student excpectedStudent = StudentFaker.getStudent();
    when(this.studentKvs.findStudentById(ID)).thenReturn(Optional.of(excpectedStudent));

    Student actualStudent = this.studentUsesCases.getStudentById(ID);

    assertThat(actualStudent).isEqualTo(excpectedStudent);
    verify(this.studentKvs).findStudentById(ID);
  }

  @Test
  void returnNotFoundExceptionWhenStudentNotFoundWhileFindStudentById() {
    when(this.studentKvs.findStudentById(ID)).thenReturn(Optional.empty());

    assertThatThrownBy(() -> this.studentUsesCases.getStudentById(ID)).isInstanceOf(
        NotFoundException.class);
    verify(this.studentKvs).findStudentById(ID);
  }

  @Test
  void deleteStudentSuccessfully() {
    Student student = StudentFaker.getStudent();

    this.studentUsesCases.deleteStudentById(ID);

    verify(this.studentKvs).deleteStudentById(ID);
  }

  @Test
  void returnAllStudentsSuccessfully() {
    List<Student> expectedStudents = List.of(StudentFaker.getStudent());
    when(this.studentKvs.findAllStudents()).thenReturn(expectedStudents);

    List<Student> actualStudents = this.studentUsesCases.getAllStudents();

    assertThat(actualStudents).isEqualTo(expectedStudents);
    verify(this.studentKvs).findAllStudents();
  }

  @Test
  void saveStudentWithAvgNotesSuccessfully() {
    Student student = StudentFaker.getStudentWithAvgNotes();

    this.studentUsesCases.saveStudent(student);

    verify(this.studentKvs).save(this.studentArgCaptor.capture());
    Student studentCaptor = this.studentArgCaptor.getValue();
    assertThat(studentCaptor.getAverageNotes()).isNotNull();
  }

  @Test
  void saveAsyncStudentWithAvgNotesSuccessfully() {
    Student student = StudentFaker.getStudentWithAvgNotes();

    this.studentUsesCases.saveStudentWithAvgNotes(student);

    verify(this.studentProducerApi).saveAsyncStudentWithAvgNotes(student);
  }
}