package ru.netak.service.user;

import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.netak.dto.user.UserFormDto;
import ru.netak.dto.user.UserLoginDto;
import ru.netak.dto.user.UserRegisterDto;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;
import ru.netak.security.SecurityUser;
import ru.netak.security.dto.RefreshTokenDto;
import ru.netak.security.dto.TokensDto;
import ru.netak.security.jwt.JwtService;
import ru.netak.security.service.CustomUserDetailService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailService userDetailService;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerUser_ValidData_ReturnsRegisterDto() {
        UserFormDto userFormDto = new UserFormDto(
                "username@gmail.com",
                "username",
                "password"
        );

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail(userFormDto.email());
        mockUser.setUsername(userFormDto.username());
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        TokensDto mockTokens = new TokensDto("access_token", "refresh_token");

        when(userService.createUser(userFormDto)).thenReturn(mockUser);
        when(jwtService.getTokens(mockUser.getEmail())).thenReturn(mockTokens);

        UserRegisterDto result = authService.registerUser(userFormDto);

        assertNotNull(result);
        assertEquals(mockUser.getId(), result.id());
        assertEquals(mockUser.getUsername(), result.username());
        assertEquals(mockTokens, result.tokens());

        verify(userService).createUser(userFormDto);
        verify(jwtService).getTokens(mockUser.getEmail());
    }

    @Test
    void loginUser_ValidData_ReturnsTokens() {
        UserLoginDto loginDto = new UserLoginDto("username@gmail.com", "password");

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("username@gmail.com");
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        SecurityUser securityUser = new SecurityUser(mockUser);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(securityUser);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        TokensDto mockTokens = new TokensDto("access_token", "refresh_token");
        when(jwtService.getTokens(securityUser.getUsername())).thenReturn(mockTokens);

        TokensDto result = authService.loginUser(loginDto);

        assertNotNull(result);
        assertEquals(mockTokens, result);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).getTokens(securityUser.getUsername());
    }

    @Test
    void refreshAccessToken_ValidRefreshToken_ReturnsNewTokens() {
        String refreshToken = "valid_refresh_token";
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken);
        String email = "username@gmail.com";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail(email);
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        SecurityUser securityUser = new SecurityUser(mockUser);

        when(jwtService.getEmailFromToken(refreshToken)).thenReturn(email);
        when(userDetailService.loadUserByUsername(email)).thenReturn(securityUser);
        when(jwtService.isRefreshTokenValid(refreshToken, securityUser)).thenReturn(true);

        TokensDto mockTokens = new TokensDto("new_access_token", refreshToken);
        when(jwtService.getRefreshedAccessToken(email, refreshToken)).thenReturn(mockTokens);

        TokensDto result = authService.refreshAccessToken(refreshTokenDto);

        assertNotNull(result);
        assertEquals(mockTokens, result);

        verify(jwtService).getEmailFromToken(refreshToken);
        verify(userDetailService).loadUserByUsername(email);
        verify(jwtService).isRefreshTokenValid(refreshToken, securityUser);
        verify(jwtService).getRefreshedAccessToken(email, refreshToken);
    }

    @Test
    void refreshAccessToken_InvalidRefreshToken_ThrowsJwtException() {
        String refreshToken = "invalid_refresh_token";
        RefreshTokenDto refreshTokenDto = new RefreshTokenDto(refreshToken);
        String email = "username@gmail.com";

        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail(email);
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        SecurityUser securityUser = new SecurityUser(mockUser);

        when(jwtService.getEmailFromToken(refreshToken)).thenReturn(email);
        when(userDetailService.loadUserByUsername(email)).thenReturn(securityUser);
        when(jwtService.isRefreshTokenValid(refreshToken, securityUser)).thenReturn(false);

        assertThrows(JwtException.class, () -> {
            authService.refreshAccessToken(refreshTokenDto);
        });

        verify(jwtService).getEmailFromToken(refreshToken);
        verify(userDetailService).loadUserByUsername(email);
        verify(jwtService).isRefreshTokenValid(refreshToken, securityUser);
        verify(jwtService, never()).getRefreshedAccessToken(anyString(), anyString());
    }
}