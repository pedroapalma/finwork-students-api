package com.ppalma.studentsapi.domain.port;

import com.ppalma.studentsapi.domain.model.Student;

public interface StudentProducerApi {

  void saveAsyncStudentWithAvgNotes(Student student);
}
