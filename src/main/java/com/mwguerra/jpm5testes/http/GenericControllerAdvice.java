package com.mwguerra.jpm5testes.http;

import com.mwguerra.jpm5testes.http.exceptions.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericControllerAdvice {
  Logger logger = LoggerFactory.getLogger(GenericControllerAdvice.class);

  @ExceptionHandler({ProductNotFoundException.class})
  public ResponseEntity<String> ProductNotFound (final ProductNotFoundException e) {
    logger.error(e.getMessage());
    return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
