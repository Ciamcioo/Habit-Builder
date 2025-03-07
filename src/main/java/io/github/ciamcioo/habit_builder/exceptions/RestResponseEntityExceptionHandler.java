package io.github.ciamcioo.habit_builder.exceptions;

import jakarta.validation.ValidationException;
import jakarta.xml.bind.ValidationEventHandler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
        HabitAlreadyExistsException.class
    })
    protected ResponseEntity<Error> badRequestExceptionHandler(RuntimeException exception, WebRequest request) {
        return new ResponseEntity<>(
                new Error(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {
            HabitNotFoundException.class
    })
    protected ResponseEntity<Error> notFoundExceptionHandler(RuntimeException exception, WebRequest request) {
        return new ResponseEntity<>(
                new Error(exception.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getAllErrors().forEach(error -> {
            errors.put(
                    ((FieldError) error).getField(),
                    error.getDefaultMessage()
            );
        });

        return new ResponseEntity<>(
            errors,
            HttpStatus.BAD_REQUEST
        );
    }
}
