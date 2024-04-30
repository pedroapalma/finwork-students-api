package com.ppalma.studentsapi.mapper;

import com.ppalma.studentsapi.entity.StudentEntity;
import com.ppalma.studentsapi.model.Student;

public interface StudentMapper {

  static Student toStudent(StudentEntity studentEntity) {
    return Student.builder()
        .dni(studentEntity.getDni())
        .name(studentEntity.getName())
        .notes(studentEntity.getNotes())
        .averageNotes(studentEntity.getAverageNotes())
        .build();
  }

  static StudentEntity toStudentEntity(Student student) {
    return StudentEntity.builder()
        .id(student.getDni())
        .dni(student.getDni())
        .name(student.getName())
        .notes(student.getNotes())
        .averageNotes(student.getAverageNotes())
        .build();
  }
}
