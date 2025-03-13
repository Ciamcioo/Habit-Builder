package io.github.ciamcioo.habit_builder.controller.exceptions;

import io.github.ciamcioo.habit_builder.service.exceptions.HabitAlreadyExistsException;
import io.github.ciamcioo.habit_builder.service.exceptions.HabitNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {
        HabitAlreadyExistsException.class
    })
    protected ResponseEntity<io.github.ciamcioo.habit_builder.controller.exceptions.Error> badRequestExceptionHandler(RuntimeException exception, WebRequest request) {
        return new ResponseEntity<>(
                new io.github.ciamcioo.habit_builder.controller.exceptions.Error(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {
            HabitNotFoundException.class
    })
    protected ResponseEntity<io.github.ciamcioo.habit_builder.controller.exceptions.Error> notFoundExceptionHandler(RuntimeException exception, WebRequest request) {
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
