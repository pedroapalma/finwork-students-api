package com.ppalma.studentsapi.infrastructure.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.ppalma.studentsapi.domain.model.Student;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

class StudentUtilsTest {

  @Test
  void buildStudentWithAvgNotesSuccessfully() {
    // Given
    Student student = Student.builder()
        .dni("1234567890")
        .name("John Doe")
        .notes(Arrays.asList(8.5, 7.0, 9.0))
        .build();

    // When
    Student result = StudentUtils.buildStudentWithAvgNotes(student);

    // Then
    assertThat(result.getDni()).isEqualTo("1234567890");
    assertThat(result.getName()).isEqualTo("John Doe");
    assertThat(result.getNotes()).containsExactly(8.5, 7.0, 9.0);
    assertThat(result.getAverageNotes()).isEqualTo(8.166666666666666);
  }

  @Test
  void calculateAvgNotesSuccessfully() {
    // Given
    Student student = Student.builder()
        .dni("1234567890")
        .name("John Doe")
        .notes(Arrays.asList(8.5, 7.0, 9.0))
        .build();

    // When
    double result = StudentUtils.calculateAvgNotes(student);

    // Then
    assertThat(result).isEqualTo(8.166666666666666);
  }
}
