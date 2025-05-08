package com.cobanoglu.anomalydetectionservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AnomalySaveException extends RuntimeException {
    public AnomalySaveException(String message) {
        super(message);
    }
    public AnomalySaveException(String message, Throwable cause) {
        super(message, cause);
    }
}
