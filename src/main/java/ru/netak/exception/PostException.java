package ru.netak.exception;

import org.springframework.http.HttpStatus;

public class PostException extends AbstractException {

    private PostException(String errorCode, String message) {
        super(errorCode, message, HttpStatus.NOT_FOUND);
    }

    private PostException(String errorCode, String message, HttpStatus httpStatus) {
        super(errorCode, message, httpStatus);
    }

    private PostException(String errorCode, String message, Throwable cause) {
        super(errorCode, message, HttpStatus.CONFLICT, cause);
    }

    public static PostException userNotFound(String email) {
        return new PostException(
                "POST_USER_NOT_FOUND",
                "Пользователь " + email + " не найден"
        );
    }

    public static PostException userNotFound(long id) {
        return new PostException(
                "POST_USER_NOT_FOUND",
                "Пользователь с id" + id + " не найден"
        );
    }

    public static PostException postNotFound(long postId) {
        return new PostException(
                "POST_NOT_FOUND",
                "Пост с id " + postId + " не найден"
        );
    }

    public static PostException accessToPost() {
        return new PostException(
                "POST_ACCESS",
                "У вас нет прав на управление этим постом",
                HttpStatus.FORBIDDEN
        );
    }
}
