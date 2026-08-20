package ru.netak.exception.model;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

public record ErrorResponseValidate(
        String errorCode,
        Map<String, List<String>> errors,
        HttpStatus httpStatus
) {
}
