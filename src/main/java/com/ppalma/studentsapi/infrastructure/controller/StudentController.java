package com.ppalma.studentsapi.infrastructure.controller;

import com.ppalma.studentsapi.application.usescases.StudentUsesCases;
import com.ppalma.studentsapi.domain.model.Student;
import jakarta.validation.Valid;
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

  private final StudentUsesCases studentUsesCases;

  @PostMapping
  @ResponseStatus(HttpStatus.OK)
  public void saveStudent(@Valid @RequestBody Student student) {
    this.studentUsesCases.saveStudent(student);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public Student getStudentById(@PathVariable String id) {
    return this.studentUsesCases.getStudentById(id);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public void deleteStudent(@PathVariable String id) {
    this.studentUsesCases.deleteStudentById(id);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Student> getStudents() {
    return this.studentUsesCases.getAllStudents();
  }

  @PostMapping("/average-notes")
  @ResponseStatus(HttpStatus.OK)
  public void saveStudentWithAvgNotes(@Valid @RequestBody Student student) {
    this.studentUsesCases.saveStudentWithAvgNotes(student);
  }
}
