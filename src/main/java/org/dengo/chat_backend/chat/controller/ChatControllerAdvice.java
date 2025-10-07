package org.dengo.chat_backend.chat.controller;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ChatControllerAdvice {
  
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e) {
    
    Map<String, String> errorResponse = new HashMap<>();
    
    errorResponse.put("message", e.getMessage());
    
    return ResponseEntity.badRequest().body(errorResponse);
  }
  
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEntityNotFoundException(EntityNotFoundException e) {
    
    Map<String, String> errorResponse = new HashMap<>();
    
    errorResponse.put("message", e.getMessage());
    
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }
}
