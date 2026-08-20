package ru.netak.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netak.dto.user.UserDto;
import ru.netak.dto.user.UserFormDto;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;
import ru.netak.exception.UserException;
import ru.netak.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(UserFormDto userDto) {
        if (userRepository.existsUserByEmail(userDto.email())) {
            throw UserException.registrationEmailTaken(userDto.email());
        }

        if (userRepository.existsUserByUsername(userDto.username())) {
            throw UserException.registrationUsernameTaken(userDto.username());
        }

        User user = new User(
                userDto.email(),
                userDto.username(),
                Role.USER,
                passwordEncoder.encode(userDto.password())
        );

        userRepository.save(user);

        return user;
    }

    @Transactional
    public String deleteUserById(long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(UserException::userNotFound);

        userRepository.delete(user);

        return "Пользователь с id " + id + " удален";
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getUsername()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(long id) {
        User user = userRepository.findUserById(id)
                .orElseThrow(UserException::userNotFound);

        return new UserDto(user.getId(), user.getUsername());
    }
}
