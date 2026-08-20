package ru.netak.exception.model;

import org.springframework.http.HttpStatus;

public record ErrorResponse(
        String errorCode,
        String message,
        HttpStatus httpStatus
) {
}
