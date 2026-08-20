package ru.netak.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.netak.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByEmail(String email);
    Optional<User> findUserById(long id);
    boolean existsUserByUsername(String username);
    boolean existsUserByEmail(String email);
}
