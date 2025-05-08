package com.cobanoglu.anomalydetectionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAirDataException extends RuntimeException {
    public InvalidAirDataException(String message) {
        super(message);
    }
}
