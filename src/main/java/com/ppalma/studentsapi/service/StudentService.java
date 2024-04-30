package com.ppalma.studentsapi.service;

import com.ppalma.studentsapi.model.Student;
import java.util.List;

public interface StudentService {

  void save(Student student);

  Student getStudentById(String id);

  void delete(Student Student);

  List<Student> getAllStudents();

}
