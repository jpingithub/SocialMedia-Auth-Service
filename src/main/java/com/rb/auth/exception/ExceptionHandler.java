package com.rb.auth.exception;

import com.rb.auth.dto.ExceptionResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.text.ParseException;
import java.time.Instant;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(TokenException.class)
    public ResponseEntity<ExceptionResponse> handleTokenException(TokenException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimeStamp(Instant.now().toString());
        exceptionResponse.setPath(request.getDescription(true));
        return new ResponseEntity<>(exceptionResponse, HttpStatusCode.valueOf(400));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFoundException(UserException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimeStamp(Instant.now().toString());
        exceptionResponse.setPath(request.getDescription(true));
        return new ResponseEntity<>(exceptionResponse, HttpStatusCode.valueOf(400));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ParseException.class)
    public ResponseEntity<ExceptionResponse> handleParseException(ParseException ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse();
        exceptionResponse.setMessage(ex.getMessage());
        exceptionResponse.setTimeStamp(Instant.now().toString());
        exceptionResponse.setPath(request.getDescription(true));
        return new ResponseEntity<>(exceptionResponse, HttpStatusCode.valueOf(400));
    }

}
