package com.ppalma.studentsapi.domain.port;

public interface StudentConsumer<T> {

  void saveStudentWithAvgNotes(T student);

}
