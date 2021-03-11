package com.example.logbookerror;

import static org.springframework.http.ResponseEntity.status;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class MyControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        log.error("Exception occurred", e);
        return status(500).body("Something has gone horribly wrong: " + e.getMessage());
    }

}
