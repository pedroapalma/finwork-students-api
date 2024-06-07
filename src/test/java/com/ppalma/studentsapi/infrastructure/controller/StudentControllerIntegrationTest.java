package com.ppalma.studentsapi.infrastructure.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ppalma.studentsapi.application.usescases.StudentUsesCases;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.fake.StudentFaker;
import com.ppalma.studentsapi.infrastructure.entity.StudentEntity;
import com.ppalma.studentsapi.utils.InitializeDynamoDB;
import com.ppalma.studentsapi.utils.IntegrationTest;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;


@IntegrationTest
@InitializeDynamoDB
public class StudentControllerIntegrationTest {

  public static final String ID = "123456";
  public static final String STUDENTS_URI = "/api/v1/students";
  public static final String GET_STUDENT_BY_ID_URI = "/api/v1/students/{id}";
  public static final String POST_STUDENT_WITH_AVERAGE_NOTES = "/api/v1/students/average-notes";
  public static final String SCOPE_STUDENTS_BASIC = "SCOPE_students/basic";
  public static final String SCOPE_STUDENTS_AVERAGE_NOTES = "SCOPE_students/average-notes";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @SpyBean
  private StudentUsesCases studentUsesCases;

  @SpyBean
  private DynamoDBMapper dynamoDBMapper;

  @BeforeEach
  void setUp() {
    this.clearDynamoDb();
  }

  private void clearDynamoDb() {
    Optional.ofNullable(this.dynamoDBMapper.load(StudentEntity.class, ID))
        .ifPresent(this.dynamoDBMapper::delete);
  }

  @Test
  void givenRequestIsAnonymous_whenGetGreet_thenUnauthorized() throws Exception {
    Student student = StudentFaker.getStudent();

    this.mockMvc.perform(post(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isOk());
  }

  @Test
  public void returnOkAndSaveStudentSuccessfully() throws Exception {
    Student student = StudentFaker.getStudent();

    this.mockMvc.perform(post(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isOk());

    verify(this.studentUsesCases, times(1)).saveStudent(student);
  }

  @Test
  public void returnOkAndStudentWhenFindStudentById() throws Exception {
    Student student = StudentFaker.getStudent();
    this.dynamoDBMapper.save(StudentFaker.getStudentEntity());

    this.mockMvc.perform(get(GET_STUDENT_BY_ID_URI, ID)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC))))
        .andExpect(status().isOk())
        .andExpect(content().json(this.objectMapper.writeValueAsString(student)));
  }

  @Test
  public void returnOkAndDeleteStudentSuccessfully() throws Exception {
    this.dynamoDBMapper.save(StudentFaker.getStudentEntity());

    this.mockMvc.perform(delete(GET_STUDENT_BY_ID_URI, ID)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC))))
        .andExpect(status().isOk());

    verify(this.studentUsesCases, times(1)).deleteStudentById(ID);
  }

  @Test
  public void returnOkAndAllStudentsSuccessfully() throws Exception {
    List<Student> students = List.of(StudentFaker.getStudent());
    this.dynamoDBMapper.save(StudentFaker.getStudentEntity());

    this.mockMvc.perform(get(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC))))
        .andExpect(status().isOk())
        .andExpect(content().json(this.objectMapper.writeValueAsString(students)));
  }

  @Test
  public void saveAsyncStudentWithAvgNotesSuccessfully() throws Exception {
    Student student = StudentFaker.getStudent();
    doNothing().when(this.studentUsesCases).saveStudentWithAvgNotes(student);

    this.mockMvc.perform(post(POST_STUDENT_WITH_AVERAGE_NOTES)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_AVERAGE_NOTES)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isOk());

    verify(this.studentUsesCases, times(1)).saveStudentWithAvgNotes(student);
  }

  @Test
  public void returnNotFoundAndStudentWhenFindStudentById() throws Exception {
    this.mockMvc.perform(get(GET_STUDENT_BY_ID_URI, ID)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC))))
        .andExpect(status().isNotFound());
  }

  @Test
  public void returnForbiddenWhenScopeIsInvalid() throws Exception {
    this.mockMvc.perform(get(GET_STUDENT_BY_ID_URI, ID)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority("SCOPE_INVALID"))))
        .andExpect(status().isForbidden());
  }

  @Test
  public void returnUnauthorizedWhenCredentialsIsInvalid() throws Exception {
    this.mockMvc.perform(get(GET_STUDENT_BY_ID_URI, ID))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void returnInternalErrorWhenSaveStudentFails() throws Exception {
    Student student = StudentFaker.getStudent();
    doThrow(RuntimeException.class).when(this.studentUsesCases).saveStudent(student);

    this.mockMvc.perform(post(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void returnInternalErrorAndAmazonServiceExceptionWhenSaveStudentFails() throws Exception {
    Student student = StudentFaker.getStudent();
    AmazonServiceException amazonServiceException = new AmazonServiceException("");
    amazonServiceException.setStatusCode(500);
    doThrow(amazonServiceException).when(this.studentUsesCases).saveStudent(student);

    this.mockMvc.perform(post(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void returnInternalErrorAndAmazonClientExceptionWhenSaveStudentFails() throws Exception {
    Student student = StudentFaker.getStudent();
    doThrow(AmazonClientException.class).when(this.studentUsesCases).saveStudent(student);

    this.mockMvc.perform(post(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isInternalServerError());
  }

  @Test
  public void returnBadRequestInSaveStudentWhenInputIsNotValid() throws Exception {
    Student student = new Student();

    this.mockMvc.perform(post(STUDENTS_URI)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_BASIC)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void returnBadRequestInSaveStudentWithAvgNotesWhenInputIsNotValid() throws Exception {
    Student student = new Student();

    this.mockMvc.perform(post(POST_STUDENT_WITH_AVERAGE_NOTES)
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .authorities(new SimpleGrantedAuthority(SCOPE_STUDENTS_AVERAGE_NOTES)))
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(student)))
        .andExpect(status().isBadRequest());
  }

}
