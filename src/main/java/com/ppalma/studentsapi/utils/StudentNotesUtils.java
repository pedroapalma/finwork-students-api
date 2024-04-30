package com.ppalma.studentsapi.utils;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

import com.ppalma.studentsapi.model.Student;

public interface StudentNotesUtils {

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
