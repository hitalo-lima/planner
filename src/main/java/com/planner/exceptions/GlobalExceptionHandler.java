package com.planner.exceptions;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgument(IllegalArgumentException ex) {
        ErrorMessage errorMessage = new ErrorMessage(ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorData>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<ValidationErrorData> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationErrorData(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleInvalidJson(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorMessage("Invalid JSON payload. Please check the request format."));
    }

    private record ErrorMessage(String message) {
    }

    private record ValidationErrorData(String field, String message) {
    }
}
