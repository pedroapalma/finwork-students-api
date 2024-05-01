package com.ppalma.studentsapi.repository;

import com.ppalma.studentsapi.model.Student;
import java.util.List;
import java.util.Optional;

public interface StudentRepository {

  void save(Student student);

  Optional<Student> findStudentById(String id);

  void delete(Student Student);

  List<Student> findAllStudents();

  void saveWithAvgNotes(Student student);

}
