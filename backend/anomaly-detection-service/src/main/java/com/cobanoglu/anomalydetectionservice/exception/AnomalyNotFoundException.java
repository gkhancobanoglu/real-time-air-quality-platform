package com.cobanoglu.anomalydetectionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AnomalyNotFoundException extends RuntimeException {
  public AnomalyNotFoundException(String message) {
    super(message);
  }
}
