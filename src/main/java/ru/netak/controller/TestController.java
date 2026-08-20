package ru.netak.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.netak.dto.user.UserDto;

@RestController
public class TestController {

    @GetMapping("/")
    public ResponseEntity<UserDto> test() {
        return ResponseEntity.status(201).body(new UserDto(-1, "-1"));
    }
}
