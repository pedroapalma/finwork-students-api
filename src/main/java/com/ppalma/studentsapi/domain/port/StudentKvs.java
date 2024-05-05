package com.ppalma.studentsapi.domain.port;

import com.ppalma.studentsapi.domain.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentKvs {

  void save(Student student);

  Optional<Student> findStudentById(String id);

  void delete(Student Student);

  List<Student> findAllStudents();

}
