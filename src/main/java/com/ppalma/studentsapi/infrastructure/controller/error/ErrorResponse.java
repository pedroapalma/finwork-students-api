package com.ppalma.studentsapi.infrastructure.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private String message;

  private HttpStatus status;
}
