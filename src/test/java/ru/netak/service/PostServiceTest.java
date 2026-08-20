package ru.netak.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netak.dto.post.PostDto;
import ru.netak.dto.post.PostFormDto;
import ru.netak.entity.Post;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;
import ru.netak.exception.PostException;
import ru.netak.repository.PostRepository;
import ru.netak.security.service.SecurityService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private SecurityService securityService;

    @InjectMocks
    private PostService postService;

    @Test
    void createPost_ValidData_SavePost() {
        PostFormDto postFormDto = new PostFormDto(
                "Title",
                "Description"
        );
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("username@gmail.com");
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        Post mockPost = new Post(mockUser, postFormDto.title(), postFormDto.description());

        when(securityService.getUserFromContext()).thenReturn(mockUser);
        when(postRepository.save(mockPost)).thenReturn(mockPost);

        PostDto result = postService.createPost(postFormDto);

        assertNotNull(result);
        assertEquals(mockPost.getId(), result.id());
        assertEquals(mockPost.getUser().getId(), result.user().id());
        assertEquals(mockPost.getUser().getUsername(), result.user().username());
        assertEquals(mockPost.getTitle(), result.title());
        assertEquals(mockPost.getDescription(), result.description());

        verify(postRepository).save(mockPost);
    }

    @Test
    void deletePost_RoleAdmin_DeletePost() {
        long id = 1;
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("username@gmail.com");
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.ADMIN);

        Post mockPost = new Post(mockUser, "Title", "Description");
        mockPost.setId(id);

        when(securityService.getUserFromContext()).thenReturn(mockUser);
        when(postRepository.findPostById(id)).thenReturn(Optional.of(mockPost));

        String result = postService.deletePost(id);
        String expected = "ADMIN Пост " + id + " удален";

        assertEquals(expected, result);

        verify(postRepository).delete(mockPost);
    }

    @Test
    void deletePost_Author_DeletePost() {
        long id = 1;
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("username@gmail.com");
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        Post mockPost = new Post(mockUser, "Title", "Description");
        mockPost.setId(id);

        when(securityService.getUserFromContext()).thenReturn(mockUser);
        when(postRepository.findPostById(id)).thenReturn(Optional.of(mockPost));

        String result = postService.deletePost(id);
        String expected = "Пост с id " + id + " удален";

        assertEquals(expected, result);

        verify(postRepository).delete(mockPost);
    }

    @Test
    void deletePost_AnotherUser_ThrowsAccessPost() {
        long id = 1;
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("username@gmail.com");
        mockUser.setUsername("username");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        User mockAuthor = new User();
        mockUser.setId(2L);
        mockUser.setEmail("author@gmail.com");
        mockUser.setUsername("author");
        mockUser.setHashPassword("encode_password");
        mockUser.setRole(Role.USER);

        Post mockPost = new Post(mockAuthor, "Title", "Description");
        mockPost.setId(id);

        when(securityService.getUserFromContext()).thenReturn(mockUser);
        when(postRepository.findPostById(id)).thenReturn(Optional.of(mockPost));

        assertThrows(PostException.class, () -> {
            postService.deletePost(id);
        });

        verify(postRepository, never()).delete(any());
    }
}