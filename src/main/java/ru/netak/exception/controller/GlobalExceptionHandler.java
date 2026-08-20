package ru.netak.exception.controller;

import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.netak.exception.AbstractException;
import ru.netak.exception.model.ErrorResponse;
import ru.netak.exception.model.ErrorResponseValidate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AbstractException.class)
    public ResponseEntity<ErrorResponse> handleAbstractException(AbstractException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(
                e.getErrorCode(),
                e.getMessage(),
                e.getHttpStatus()
        ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
                "AUTH_BAD_CREDENTIALS",
                "Неверный логин или пароль",
                HttpStatus.UNAUTHORIZED
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseValidate> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, List<String>> errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseValidate(
                "NOT_VALID_VALUE",
                errorMessage,
                HttpStatus.BAD_REQUEST
        ));
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationService(InternalAuthenticationServiceException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "INTERNAL_AUTH_SERVICE_FAIL",
                "Ошибка сервиса аутентификации",
                HttpStatus.INTERNAL_SERVER_ERROR
        ));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwt(JwtException e) {
        log.error("Error JWT - {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
                "TOKEN_EXCEPTION",
                "Ошибка с токеном",
                HttpStatus.UNAUTHORIZED
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception e) {
        log.error("Error unkown - {}", e.getClass());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                "INTERNAL_ERROR",
                "Проблема внутри приложения",
                HttpStatus.INTERNAL_SERVER_ERROR
        ));
    }
}
