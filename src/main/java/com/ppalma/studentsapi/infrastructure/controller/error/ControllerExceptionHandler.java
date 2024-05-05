package com.ppalma.studentsapi.infrastructure.controller.error;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.ppalma.studentsapi.application.exception.NotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

  public static final String UNEXPECTED_ERROR = "Unexpected Error";

  @ExceptionHandler(value = {AmazonServiceException.class, AmazonClientException.class})
  public ResponseEntity<ErrorResponse> amazonException(RuntimeException ex) {
    ErrorResponse errorMessage;

    log.error(ex.getMessage(), ex);

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

    log.error(ex.getMessage(), ex);

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
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorResponse> noResourceFoundException(NoResourceFoundException ex,
      HttpServletRequest request) {

    Object responseStatus = request.getAttribute(
        "org.springframework.web.servlet.View.responseStatus");

    Set<String> fields = Set.of("dni", "name", "notes");

    String message = fields.stream()
        .map(field -> {
          Object value = request.getAttribute(field);
          return Objects.nonNull(value) ? String.format("%s %s", field, value) : null;
        })
        .filter(Objects::nonNull)
        .collect(Collectors.joining(", "));

    ErrorResponse errorMessage = new ErrorResponse(message, (HttpStatus) responseStatus);
    return new ResponseEntity<>(errorMessage, errorMessage.getStatus());
  }

}