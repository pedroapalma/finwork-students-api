package com.ppalma.studentsapi.exception;

import lombok.Getter;

@Getter
public class InternalServerException extends RuntimeException {

  public InternalServerException(String message) {
    super(message);
  }
}
