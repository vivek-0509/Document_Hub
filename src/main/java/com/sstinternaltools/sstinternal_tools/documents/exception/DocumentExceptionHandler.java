package com.sstinternaltools.sstinternal_tools.documents.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DocumentExceptionHandler {
    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<String> duplicateCategoryFound(DuplicateCategoryException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}

