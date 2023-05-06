package com.jasonvillar.works.register.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({org.springframework.web.bind.MethodArgumentNotValidException.class})
    public ResponseEntity<String> handleException(org.springframework.web.bind.MethodArgumentNotValidException exception) {
        StringBuilder message = new StringBuilder();

        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            if (!message.isEmpty()) {
                message.append("\n");
            }
            message.append(fieldError.getField());
            message.append(" ");
            message.append(fieldError.getDefaultMessage());
        });

        return ResponseEntity
                .badRequest()
                .body(message.toString());
    }
}
