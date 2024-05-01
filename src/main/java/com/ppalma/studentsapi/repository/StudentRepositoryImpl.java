package com.ppalma.studentsapi.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.ppalma.studentsapi.entity.StudentEntity;
import com.ppalma.studentsapi.mapper.StudentMapper;
import com.ppalma.studentsapi.model.Student;
import com.ppalma.studentsapi.rest.StudentNotesRestClient;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

  private final DynamoDBMapper dynamoDBMapper;

  private final StudentNotesRestClient studentNotesRestClient;

  public StudentRepositoryImpl(AmazonDynamoDB amazonDynamoDB,
      StudentNotesRestClient studentNotesRestClient) {
    this.dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
    this.studentNotesRestClient = studentNotesRestClient;
  }

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
    return this.dynamoDBMapper.scan(StudentEntity.class, scanExpression).stream()
        .map(StudentMapper::toStudent).toList();
  }

  @Override
  public void saveWithAvgNotes(Student student) {
    this.studentNotesRestClient.sendMsgToTopicForAvgNotes(student);
  }
}
