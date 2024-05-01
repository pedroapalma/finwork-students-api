package com.ppalma.studentsapi.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private String message;

  private HttpStatus status;
}
