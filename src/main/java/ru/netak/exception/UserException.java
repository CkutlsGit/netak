package ru.netak.exception;

import org.springframework.http.HttpStatus;

public class UserException extends AbstractException {

    private UserException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.CONFLICT);
    }

    private UserException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

    private UserException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, HttpStatus.CONFLICT, cause);
    }

    public static UserException registrationUsernameTaken(String username) {
        return new UserException(
                "REGISTRATION_USERNAME_TAKEN",
                "Никнейм - " + username + " занят"
        );
    }

    public static UserException registrationEmailTaken(String email) {
        return new UserException(
                "REGISTRATION_EMAIL_TAKEN",
                "Почта - " + email + " уже занята"
        );
    }

    public static UserException userNotFound() {
        return new UserException(
                "USER_NOT_FOUND",
                "Юзер не найден",
                HttpStatus.NOT_FOUND
        );
    }

    public static UserException userNotFound(String username) {
        return new UserException(
                "USER_NOT_FOUND",
                "Юзернейм - " + username + " не найден",
                HttpStatus.NOT_FOUND
        );
    }
}
