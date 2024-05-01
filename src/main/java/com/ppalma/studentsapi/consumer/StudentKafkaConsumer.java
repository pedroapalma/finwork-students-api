package com.ppalma.studentsapi.consumer;

import com.ppalma.studentsapi.model.Student;
import com.ppalma.studentsapi.service.StudentService;
import com.ppalma.studentsapi.utils.StudentNotesUtils;
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
public class StudentKafkaConsumer {

  private final StudentService studentService;

  @KafkaListener(topics = "${kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "STUDENT_CONTAINER_FACTORY")
  public void consume(@Payload Student studentMsg,
      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
      @Header(KafkaHeaders.OFFSET) int offset, Acknowledgment ack) {

    log.info("received msg=[{}] partition-{} offset-{}", studentMsg, partition, offset);

    this.studentService.save(StudentNotesUtils.buildStudentWithAvgNotes(studentMsg));

    ack.acknowledge();
  }
}
