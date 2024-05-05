package com.ppalma.studentsapi.application.usescases;

import com.ppalma.studentsapi.domain.model.Student;
import java.util.List;

public interface StudentUsesCases {

  void save(Student student);

  Student getStudentById(String id);

  void delete(Student Student);

  List<Student> getAllStudents();

  void saveWithAvgNotes(Student student);

}
