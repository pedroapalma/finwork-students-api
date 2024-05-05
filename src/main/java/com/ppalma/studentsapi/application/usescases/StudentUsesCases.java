package com.ppalma.studentsapi.application.usescases;

import com.ppalma.studentsapi.domain.model.Student;
import java.util.List;

public interface StudentUsesCases {

  void saveStudent(Student student);

  Student getStudentById(String id);

  void deleteStudent(Student Student);

  List<Student> getAllStudents();

  void saveStudentWithAvgNotes(Student student);

}
