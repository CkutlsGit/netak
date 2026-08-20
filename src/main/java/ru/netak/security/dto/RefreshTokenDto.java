package ru.netak.security.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenDto(
        @NotBlank(message = "Рефреш токен обязателен к заполнению")
        String refreshToken
) {
}
