package ru.netak.dto.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
public record PostFormDto(
        @NotBlank(message = "Заголовок обязателен к заполнению")
        @Size(min = 3, max = 70, message = "Заголовок должен быть минимум от 3 символов и не больше 70 символов")
        String title,

        @NotBlank(message = "Описание обязательно к заполнению")
        String description
) {
}
