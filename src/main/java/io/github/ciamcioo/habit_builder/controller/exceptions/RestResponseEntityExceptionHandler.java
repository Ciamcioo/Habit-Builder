package io.github.ciamcioo.habit_builder.controller.exceptions;

import io.github.ciamcioo.habit_builder.aspect.annotation.EnableMethodLogging;
import io.github.ciamcioo.habit_builder.exception.*;
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

    public RestResponseEntityExceptionHandler() {
    }

    @ExceptionHandler(value = {
        HabitAlreadyExistsException.class,
        UserAlreadyExistsException.class,
        MappingException.class
    })
    @EnableMethodLogging
    protected ResponseEntity<Error> badRequestExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(
                new Error(exception.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(value = {
            HabitNotFoundException.class,
            UserNotFoundException.class
    })
    @EnableMethodLogging
    protected ResponseEntity<Error> notFoundExceptionHandler(RuntimeException exception) {
        return new ResponseEntity<>(
                new Error(exception.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @Override
    @EnableMethodLogging

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception,  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        exception.getAllErrors().forEach(error -> errors.put(
                ((FieldError) error).getField(),
                error.getDefaultMessage()
        ));

        return new ResponseEntity<>(
            errors,
            HttpStatus.BAD_REQUEST
        );
    }


    @ExceptionHandler(value = {
            RuntimeException.class
    })
    @EnableMethodLogging
    protected ResponseEntity<Object> iternalServerExceptionHandler(RuntimeException exception, WebRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("server message", "Internal Server error");
        response.put("exception message", exception.getMessage());
        response.put("endpoint", request.getContextPath());
        response.put("contact", "Feel free to contact our support using email: xyz@gmail.com");

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

}
