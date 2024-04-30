package com.ppalma.studentsapi.service;

import com.ppalma.studentsapi.model.Student;
import com.ppalma.studentsapi.repository.StudentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

  private final StudentRepository studentRepository;

  @Override
  public void save(Student student) {
    this.studentRepository.save(student);
  }

  @Override
  public Student getStudentById(String id) {
    return this.studentRepository.findStudentById(id);
  }

  @Override
  public void delete(Student Student) {
    this.studentRepository.delete(Student);
  }

  @Override
  public List<Student> getAllStudents() {
    return this.studentRepository.findAllStudents();
  }
}
