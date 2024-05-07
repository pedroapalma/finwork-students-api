package com.ppalma.studentsapi.infrastructure.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class BadRequestException extends RuntimeException {

  private final BindingResult bindingResult;

  public BadRequestException(BindingResult bindingResult) {
    this.bindingResult = bindingResult;
  }

  public BadRequestException(String message, BindingResult bindingResult) {
    super(message);
    this.bindingResult = bindingResult;
  }
}
