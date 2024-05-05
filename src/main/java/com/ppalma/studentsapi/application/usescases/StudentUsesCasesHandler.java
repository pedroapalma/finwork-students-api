package com.ppalma.studentsapi.application.usescases;

import com.ppalma.studentsapi.application.exception.NotFoundException;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.domain.port.StudentKvs;
import com.ppalma.studentsapi.domain.port.StudentProducerApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentUsesCasesHandler implements StudentUsesCases {

  private final StudentKvs studentKvs;

  private final StudentProducerApi studentProducerApi;

  @Override
  public void save(Student student) {
    this.studentKvs.save(student);
  }

  @Override
  public Student getStudentById(String id) {
    return this.studentKvs.findStudentById(id).orElseThrow(
        () -> new NotFoundException(String.format("Student Not Found, id: %s", id)));
  }

  @Override
  public void delete(Student Student) {
    this.studentKvs.delete(Student);
  }

  @Override
  public List<Student> getAllStudents() {
    return this.studentKvs.findAllStudents();
  }

  @Override
  public void saveWithAvgNotes(Student student) {
    this.studentProducerApi.saveAsyncStudentWithAvgNotes(student);
  }
}
