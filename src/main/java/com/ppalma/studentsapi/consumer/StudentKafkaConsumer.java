package com.ppalma.studentsapi.consumer;

import com.ppalma.studentsapi.model.Student;
import org.springframework.kafka.support.Acknowledgment;

public interface StudentKafkaConsumer {

  void consume(Student studentMsg, int partition, int offset, Acknowledgment ack);
}
