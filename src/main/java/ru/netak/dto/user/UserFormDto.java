package ru.netak.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserFormDto(
        @NotBlank(message = "Почта обязательна к заполнению")
        @Email(message = "Некорректный формат почты")
        String email,

        @NotBlank(message = "Юзернейм обязателен к заполнению")
        @Size(min = 3, max = 30, message = "Имя пользователя должно быть минимум 3 символа и не больше 30 символов")
        String username,

        @NotBlank(message = "Пароль обязателен к заполнению")
        @Size(min = 6, message = "Минимальная длинна пароля 6 символов")
        String password
) {
}
