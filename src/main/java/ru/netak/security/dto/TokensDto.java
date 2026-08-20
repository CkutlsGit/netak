package ru.netak.security.dto;

public record TokensDto(
        String accessToken,
        String refreshToken
) {
}
