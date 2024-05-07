package com.ppalma.studentsapi.infrastructure.controller.error;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.ppalma.studentsapi.application.exception.NotFoundException;
import com.ppalma.studentsapi.infrastructure.exception.BadRequestException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  public static final String UNEXPECTED_ERROR = "Unexpected Error";

  @ExceptionHandler(value = {AmazonServiceException.class, AmazonClientException.class})
  public ResponseEntity<ErrorResponse> amazonException(RuntimeException ex) {
    ErrorResponse errorMessage;

    if (ex instanceof AmazonServiceException awsEx) {
      errorMessage = new ErrorResponse(awsEx.getMessage(),
          HttpStatus.valueOf(awsEx.getStatusCode()));
    } else {
      errorMessage = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex) {
    ErrorResponse errorMessage = new ErrorResponse(ex.getMessage(),
        HttpStatus.NOT_FOUND);

    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> exception(Exception ex) {

    log.error(ex.getMessage(), ex);

    ErrorResponse errorMessage = new ErrorResponse(UNEXPECTED_ERROR,
        HttpStatus.INTERNAL_SERVER_ERROR);
    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(value = {BadRequestException.class})
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      BadRequestException ex) {
    BindingResult bindingResul = ex.getBindingResult();

    ErrorResponse errorMessage = new ErrorResponse(this.getErrorMessage(bindingResul),
        HttpStatus.BAD_REQUEST);
    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

  private String getErrorMessage(BindingResult bindingResult) {
    return bindingResult.getAllErrors().stream()
        .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
        .collect(Collectors.joining(", "));
  }

}