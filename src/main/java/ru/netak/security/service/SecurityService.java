package ru.netak.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.netak.entity.User;
import ru.netak.exception.UserException;
import ru.netak.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SecurityService {

    private final UserRepository userRepository;

    public User getUserFromContext() {
        return userRepository.findUserByEmail(
                SecurityContextHolder.getContext().getAuthentication().getName()
        )
                .orElseThrow(UserException::userNotFound);
    }
}
