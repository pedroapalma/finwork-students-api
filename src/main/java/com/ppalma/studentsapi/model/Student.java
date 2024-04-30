package com.ppalma.studentsapi.model;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

  private String dni;

  private String name;

  private List<Double> notes;

  private Double averageNotes;
}
