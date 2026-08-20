package ru.netak.dto.user;

import ru.netak.security.dto.TokensDto;

public record UserRegisterDto(
        long id,
        String username,
        TokensDto tokens
) {
}
