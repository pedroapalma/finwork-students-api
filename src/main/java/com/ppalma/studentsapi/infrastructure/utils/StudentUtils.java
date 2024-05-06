package com.ppalma.studentsapi.infrastructure.utils;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import com.ppalma.studentsapi.domain.model.Student;

public interface StudentUtils {

  static Student buildStudentWithAvgNotes(Student student) {
    return Student.builder()
        .dni(student.getDni())
        .name(student.getName())
        .notes(student.getNotes())
        .averageNotes(calculateAvgNotes(student))
        .build();
  }

  static Double calculateAvgNotes(Student student) {
    return emptyIfNull(student.getNotes()).stream()
        .mapToDouble(Double::doubleValue)
        .average()
        .orElse(0.0);
  }

}
