package ru.netak.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AbstractException extends RuntimeException {
    private final String errorCode;
    private final HttpStatus httpStatus;
    private final String message;

    protected AbstractException(String errorCode, String message, HttpStatus httpStatus) {
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }

    protected AbstractException(String errorCode, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
