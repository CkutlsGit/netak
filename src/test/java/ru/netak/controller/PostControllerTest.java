package ru.netak.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import ru.netak.dto.post.PostFormDto;
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
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "username@gmail.com", roles = "USER")
    void createPost_ValidData_ResponseOk() throws Exception {
        User user = new User();
        user.setEmail("username@gmail.com");
        user.setUsername("username");
        user.setHashPassword("encode_password");
        user.setRole(Role.USER);
        userRepository.save(user);

        PostFormDto postFormDto = new PostFormDto("Title", "Description");

        mockMvc.perform(post("/api/post/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postFormDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title"));
    }

    @Test
    @WithMockUser(username = "username@gmail.com", roles = "USER")
    void createPost_InvalidData_ResponseBadRequest() throws Exception {
        User user = new User();
        user.setEmail("username@gmail.com");
        user.setUsername("username");
        user.setHashPassword("encode_password");
        user.setRole(Role.USER);
        userRepository.save(user);

        PostFormDto postFormDto = new PostFormDto("", "");

        mockMvc.perform(post("/api/post/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postFormDto)))
                .andExpect(status().isBadRequest());
    }
}