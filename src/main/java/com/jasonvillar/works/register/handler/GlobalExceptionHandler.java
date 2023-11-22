package com.jasonvillar.works.register.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
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

    @ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<String> handleAuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    @ResponseBody
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(Exception ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(ex.getMessage());
    }

    @ExceptionHandler({ MailSendException.class })
    @ResponseBody
    public ResponseEntity<String> handleMailSendException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


}
