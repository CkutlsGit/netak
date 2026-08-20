package ru.netak.service.user;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netak.dto.user.UserFormDto;
import ru.netak.dto.user.UserLoginDto;
import ru.netak.dto.user.UserRegisterDto;
import ru.netak.entity.User;
import ru.netak.security.service.CustomUserDetailService;
import ru.netak.security.SecurityUser;
import ru.netak.security.dto.RefreshTokenDto;
import ru.netak.security.dto.TokensDto;
import ru.netak.security.jwt.JwtService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final CustomUserDetailService userDetailService;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Transactional
    public TokensDto loginUser(UserLoginDto userDto) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto.email(), userDto.password());
        Authentication authentication = authenticationManager.authenticate(authToken);

        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        return jwtService.getTokens(user.getUsername());
    }

    @Transactional
    public UserRegisterDto registerUser(UserFormDto userDto) {
        User user = userService.createUser(userDto);

        return new UserRegisterDto(
                user.getId(),
                user.getUsername(),
                jwtService.getTokens(user.getEmail())
        );
    }

    public TokensDto refreshAccessToken(RefreshTokenDto refreshTokenDto) {
        String email = jwtService.getEmailFromToken(refreshTokenDto.refreshToken());

        UserDetails user = userDetailService.loadUserByUsername(email);

        if (!jwtService.isRefreshTokenValid(refreshTokenDto.refreshToken(), user)) {
            throw new JwtException("Токен не валидный");
        }

        return jwtService.getRefreshedAccessToken(email, refreshTokenDto.refreshToken());
    }
}
