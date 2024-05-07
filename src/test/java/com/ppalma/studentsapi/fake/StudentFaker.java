package com.ppalma.studentsapi.fake;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.infrastructure.entity.StudentEntity;
import java.util.List;

public interface StudentFaker {

  static Student getStudent() {
    return Student
        .builder()
        .dni("123456")
        .name("Pedro")
        .notes(List.of(1D, 2D, 3D))
        .build();
  }

  static Student getStudentWithAvgNotes() {
    return Student
        .builder()
        .dni("123456")
        .name("Pedro")
        .notes(List.of(1D, 2D, 3D))
        .averageNotes(6D)
        .build();
  }

  static String getJsonStudent() {
    try {
      return new ObjectMapper().writeValueAsString(StudentFaker.getStudent());
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  static StudentEntity getStudentEntity() {
    return StudentEntity
        .builder()
        .id("123456")
        .dni("123456")
        .name("Pedro")
        .notes(List.of(1D, 2D, 3D))
        .build();
  }

}
