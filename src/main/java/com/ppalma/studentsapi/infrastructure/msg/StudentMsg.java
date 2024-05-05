package com.ppalma.studentsapi.infrastructure.msg;

import com.ppalma.studentsapi.domain.model.Student;
import lombok.Builder;
import lombok.Data;
import org.springframework.kafka.support.Acknowledgment;

@Data
@Builder
public class StudentMsg {

  protected Integer partition;

  protected Integer offset;

  protected Acknowledgment ack;

  private Student student;

}
