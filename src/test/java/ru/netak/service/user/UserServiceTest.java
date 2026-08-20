package ru.netak.service.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.netak.dto.user.UserFormDto;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;
import ru.netak.exception.UserException;
import ru.netak.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ExistsEmail_ThrowsEmailTaken() {
        UserFormDto userFormDto = new UserFormDto(
                "username@gmail.com",
                "username",
                "password"
        );

        when(userRepository.existsUserByEmail(userFormDto.email())).thenReturn(true);

        UserException userException = assertThrows(UserException.class, () -> {
            userService.createUser(userFormDto);
        });

        String expected = "Почта - " + userFormDto.email() + " уже занята";

        assertEquals(expected, userException.getMessage());
    }

    @Test
    void createUser_ExistsUsername_ThrowsUsernameTaken() {
        UserFormDto userFormDto = new UserFormDto(
                "username@gmail.com",
                "username",
                "password"
        );

        when(userRepository.existsUserByEmail(userFormDto.email())).thenReturn(false);
        when(userRepository.existsUserByUsername(userFormDto.username())).thenReturn(true);

        UserException userException = assertThrows(UserException.class, () -> {
            userService.createUser(userFormDto);
        });

        String expected = "Никнейм - " + userFormDto.username() + " занят";

        assertEquals(expected, userException.getMessage());
    }

    @Test
    void createUser_ValidData_SaveUser() {
        UserFormDto userFormDto = new UserFormDto(
                "username@gmail.com",
                "username",
                "password"
        );

        when(userRepository.existsUserByEmail(userFormDto.email())).thenReturn(false);
        when(userRepository.existsUserByUsername(userFormDto.username())).thenReturn(false);
        when(passwordEncoder.encode(userFormDto.password())).thenReturn("encode_password");

        User result = userService.createUser(userFormDto);

        assertNotNull(result);

        verify(userRepository).save(argThat(user ->
                        user.getEmail().equals("username@gmail.com") &&
                        user.getUsername().equals("username") &&
                        user.getHashPassword().equals("encode_password") &&
                        user.getRole() == Role.USER
        ));
    }
}