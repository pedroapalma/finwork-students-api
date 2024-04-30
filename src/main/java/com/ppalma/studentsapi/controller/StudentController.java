package com.ppalma.studentsapi.controller;

import com.ppalma.studentsapi.model.Student;
import com.ppalma.studentsapi.service.StudentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/students")
public class StudentController {

  private final StudentService studentService;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void saveStudent(@RequestBody Student student) {
    this.studentService.save(student);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Student getStudentById(@PathVariable String id) {
    return this.studentService.getStudentById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStudent(@PathVariable String id) {
    Student student = this.studentService.getStudentById(id);
    this.studentService.delete(student);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Student> getStudents() {
    return this.studentService.getAllStudents();
  }
}
