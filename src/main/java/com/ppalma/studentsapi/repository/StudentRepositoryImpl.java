package com.ppalma.studentsapi.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.ppalma.studentsapi.entity.StudentEntity;
import com.ppalma.studentsapi.mapper.StudentMapper;
import com.ppalma.studentsapi.model.Student;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

  private final DynamoDBMapper dynamoDBMapper;

  public StudentRepositoryImpl(AmazonDynamoDB amazonDynamoDB) {
    this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
  }

  @Override
  public void save(Student student) {
    this.dynamoDBMapper.save(StudentMapper.toStudentEntity(student));
  }

  @Override
  public Student findStudentById(String id) {
    return StudentMapper.toStudent(this.dynamoDBMapper.load(StudentEntity.class, id));
  }

  @Override
  public void delete(Student Student) {
    this.dynamoDBMapper.delete(Student);
  }

  @Override
  public List<Student> findAllStudents() {
    DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
    return this.dynamoDBMapper.scan(StudentEntity.class, scanExpression).stream()
        .map(StudentMapper::toStudent).toList();
  }
}
