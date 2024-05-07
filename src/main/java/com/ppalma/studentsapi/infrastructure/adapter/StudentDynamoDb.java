package com.ppalma.studentsapi.infrastructure.adapter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.ppalma.studentsapi.application.mapper.StudentMapper;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.domain.port.StudentKvs;
import com.ppalma.studentsapi.infrastructure.entity.StudentEntity;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StudentDynamoDb implements StudentKvs {

  private final DynamoDBMapper dynamoDBMapper;

  @Override
  public void save(Student student) {
    this.dynamoDBMapper.save(StudentMapper.toStudentEntity(student));
  }

  @Override
  public Optional<Student> findStudentById(String id) {
    return Optional.ofNullable(this.dynamoDBMapper.load(StudentEntity.class, id))
        .map(StudentMapper::toStudent);
  }

  @Override
  public void delete(Student Student) {
    this.dynamoDBMapper.delete(Student);
  }

  @Override
  public List<Student> findAllStudents() {
    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    return Optional.ofNullable(this.dynamoDBMapper.scan(StudentEntity.class, scanExpression))
        .map(scanResult -> scanResult.stream()
            .map(StudentMapper::toStudent)
            .collect(Collectors.toList()))
        .orElse(Collections.emptyList());
  }
}
