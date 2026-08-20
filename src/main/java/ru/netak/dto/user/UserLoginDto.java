package ru.netak.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserLoginDto(
        @NotBlank(message = "Почта обязательна к заполнению")
        @Email(message = "Некорректный формат почты")
        String email,

        @NotBlank(message = "Пароль обязателен к заполнению")
        String password
) {
}
