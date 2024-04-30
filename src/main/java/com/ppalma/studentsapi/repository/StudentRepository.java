package com.ppalma.studentsapi.repository;

import com.ppalma.studentsapi.model.Student;
import java.util.List;

public interface StudentRepository {

  void save(Student student);

  Student findStudentById(String id);

  void delete(Student Student);

  List<Student> findAllStudents();

}
