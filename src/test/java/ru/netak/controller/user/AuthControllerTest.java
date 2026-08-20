package ru.netak.controller.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.netak.dto.user.UserFormDto;
import ru.netak.dto.user.UserLoginDto;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;
import ru.netak.repository.UserRepository;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registration_ValidData_ResponseOk() throws Exception {
        UserFormDto userFormDto = new UserFormDto("username@gmail.com", "username", "password");

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("username"));
    }

    @Test
    void registration_InvalidData_ResponseBadRequest() throws Exception {
        UserFormDto userFormDto = new UserFormDto("", "", "");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void registration_ExistsUser_ResponseConflict() throws Exception {
        User user = new User();
        user.setEmail("username@gmail.com");
        user.setUsername("username");
        user.setHashPassword("encode_password");
        user.setRole(Role.USER);
        userRepository.save(user);

        UserFormDto userFormDto = new UserFormDto("username@gmail.com", "username", "password");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userFormDto)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_ValidLogin_ResponseOk() throws Exception {
        String password = "password";
        String encodePassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail("username@gmail.com");
        user.setUsername("username");
        user.setHashPassword(encodePassword);
        user.setRole(Role.USER);
        userRepository.save(user);

        UserLoginDto userLoginDto = new UserLoginDto("username@gmail.com", password);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isOk());
    }

    @Test
    void login_InvalidPassword_ResponseUnauthorized() throws Exception {
        String password = "password";
        String encodePassword = passwordEncoder.encode(password);

        User user = new User();
        user.setEmail("username@gmail.com");
        user.setUsername("username");
        user.setHashPassword(encodePassword);
        user.setRole(Role.USER);
        userRepository.save(user);

        UserLoginDto userLoginDto = new UserLoginDto("username@gmail.com", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void login_UserNotCreate_ResponseUnauthorized() throws Exception {
        UserLoginDto userLoginDto = new UserLoginDto("username@gmail.com", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userLoginDto)))
                .andExpect(status().isUnauthorized());
    }
}