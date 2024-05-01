package com.ppalma.studentsapi.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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

  @NotNull
  @NotEmpty
  private String dni;

  @NotNull
  @NotEmpty
  private String name;

  @NotNull
  @NotEmpty
  private List<Double> notes;

  private Double averageNotes;
}
