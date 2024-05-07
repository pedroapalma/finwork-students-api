package com.ppalma.studentsapi.application.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.infrastructure.entity.StudentEntity;
import java.util.List;
import org.junit.jupiter.api.Test;

public class StudentMapperTest {

  @Test
  public void returnStudentWhenMapStudentEntityToStudent() {
    // Given
    StudentEntity studentEntity = StudentEntity.builder()
        .dni("123456789")
        .name("John Doe")
        .notes(List.of(85D, 90D, 95D))
        .averageNotes(90D)
        .build();

    // When
    Student student = StudentMapper.toStudent(studentEntity);

    // Then
    assertThat(student).isNotNull();
    assertThat(student.getDni()).isEqualTo(studentEntity.getDni());
    assertThat(student.getName()).isEqualTo(studentEntity.getName());
    assertThat(student.getNotes()).isEqualTo(studentEntity.getNotes());
    assertThat(student.getAverageNotes()).isEqualTo(studentEntity.getAverageNotes());
  }

  @Test
  public void returnStudentEntityWhenMapStudentToStudentEntity() {
    // Given
    Student student = Student.builder()
        .dni("123456789")
        .name("John Doe")
        .notes(List.of(85D, 90D, 95D))
        .averageNotes(90D)
        .build();

    // When
    StudentEntity studentEntity = StudentMapper.toStudentEntity(student);

    // Then
    assertThat(studentEntity).isNotNull();
    assertThat(studentEntity.getId()).isEqualTo(student.getDni());
    assertThat(studentEntity.getDni()).isEqualTo(student.getDni());
    assertThat(studentEntity.getName()).isEqualTo(student.getName());
    assertThat(studentEntity.getNotes()).isEqualTo(student.getNotes());
    assertThat(studentEntity.getAverageNotes()).isEqualTo(student.getAverageNotes());
  }
}
