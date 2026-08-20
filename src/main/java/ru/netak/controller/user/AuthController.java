package ru.netak.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netak.dto.user.UserFormDto;
import ru.netak.dto.user.UserLoginDto;
import ru.netak.dto.user.UserRegisterDto;
import ru.netak.security.dto.RefreshTokenDto;
import ru.netak.security.dto.TokensDto;
import ru.netak.service.user.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterDto> registration(@RequestBody @Valid UserFormDto registrationData) {
        return ResponseEntity.ok().body(authService.registerUser(registrationData));
    }

    @PostMapping("/login")
    public ResponseEntity<TokensDto> login(@RequestBody @Valid UserLoginDto loginData) {
        return ResponseEntity.ok().body(authService.loginUser(loginData));
    }

    @PostMapping("/refresh")
    public TokensDto refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return authService.refreshAccessToken(refreshTokenDto);
    }
}
