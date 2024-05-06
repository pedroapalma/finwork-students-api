package com.ppalma.studentsapi.infrastructure.adapter;

import com.ppalma.studentsapi.application.usescases.StudentUsesCases;
import com.ppalma.studentsapi.domain.model.Student;
import com.ppalma.studentsapi.domain.port.StudentConsumer;
import com.ppalma.studentsapi.infrastructure.msg.StudentMsg;
import com.ppalma.studentsapi.infrastructure.utils.StudentUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@EnableKafka
@RequiredArgsConstructor
public class StudentKafkaConsumer implements StudentConsumer<StudentMsg> {

  private final StudentUsesCases studentUsesCases;

  @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "STUDENT_CONTAINER_FACTORY")
  public void consume(@Payload Student student,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.OFFSET) int offset, Acknowledgment ack) {

    log.info("received msg=[{}] partition-{} offset-{}", student, partition, offset);

    StudentMsg studentMsg = StudentMsg
        .builder().offset(offset)
        .partition(partition)
        .ack(ack)
        .student(student)
        .build();

    this.saveStudentWithAvgNotes(studentMsg);
  }

  @Override
  public void saveStudentWithAvgNotes(StudentMsg studentMsg) {
    this.studentUsesCases.saveStudent(
        StudentUtils.buildStudentWithAvgNotes(studentMsg.getStudent()));
    studentMsg.getAck().acknowledge();
  }
}
