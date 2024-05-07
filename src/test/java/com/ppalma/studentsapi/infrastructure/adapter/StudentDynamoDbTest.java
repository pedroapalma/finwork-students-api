package com.ppalma.studentsapi.infrastructure.adapter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.fake.StudentFaker;
import com.ppalma.studentsapi.infrastructure.entity.StudentEntity;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StudentDynamoDbTest {

  public static final String ID = "123456";
  PaginatedScanList<StudentEntity> paginatedScanList;
  @Mock
  private DynamoDBMapper dynamoDBMapper;
  private StudentDynamoDb studentDynamoDb;

  @BeforeEach
  void setUp() {
    this.studentDynamoDb = new StudentDynamoDb(this.dynamoDBMapper);
  }

  @Test
  void saveStudentSuccessfully() {
    Student student = StudentFaker.getStudent();
    StudentEntity studentEntity = StudentFaker.getStudentEntity();

    this.studentDynamoDb.save(student);

    verify(this.dynamoDBMapper).save(studentEntity);
  }

  @Test
  void returnStudentWhenFindStudentById() {
    StudentEntity studentEntity = StudentFaker.getStudentEntity();
    when(this.dynamoDBMapper.load(StudentEntity.class, ID)).thenReturn(studentEntity);

    Optional<Student> actualOptStudent = this.studentDynamoDb.findStudentById(ID);

    assertThat(actualOptStudent).isPresent();
    Student actualStudent = actualOptStudent.get();
    assertThat(actualStudent.getDni()).isEqualTo(ID);
  }

  @Test
  void deleteStudentSuccessfully() {
    Student student = StudentFaker.getStudent();

    this.studentDynamoDb.delete(student);

    verify(this.dynamoDBMapper).delete(any());
  }

  @Test
  void returnAllStudentsSuccessfully() {
    when(this.dynamoDBMapper.scan(eq(StudentEntity.class), any(DynamoDBScanExpression.class)))
        .thenReturn(this.paginatedScanList);

    this.studentDynamoDb.findAllStudents();

    verify(this.dynamoDBMapper).scan(eq(StudentEntity.class), any(DynamoDBScanExpression.class));
  }
}