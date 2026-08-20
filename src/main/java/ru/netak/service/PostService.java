package ru.netak.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.netak.dto.post.PostDto;
import ru.netak.dto.post.PostFormDto;
import ru.netak.entity.Post;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;
import ru.netak.exception.PostException;
import ru.netak.repository.PostRepository;
import ru.netak.security.service.SecurityService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final SecurityService securityService;

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    public PostDto getPostById(long id) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(() -> PostException.postNotFound(id));

        return PostDto.fromEntity(post);
    }

    public List<PostDto> getPostsUserById(long id) {
        return postRepository.findPostByUserId(id)
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostDto createPost(PostFormDto postDto) {
        User user = securityService.getUserFromContext();

        Post post = postRepository.save(new Post(
                user,
                postDto.title(),
                postDto.description()
        ));

        return PostDto.fromEntity(post);
    }

    @Transactional
    public String deletePost(long id) {
        Post post = postRepository.findPostById(id)
                .orElseThrow(() -> PostException.postNotFound(id));

        User user = securityService.getUserFromContext();

        if (user.getRole() == Role.ADMIN) {
            postRepository.delete(post);
            return "ADMIN Пост " + id + " удален";
        }

        if (post.getUser().getId() != user.getId()) {
            throw PostException.accessToPost();
        }

        postRepository.delete(post);
        return "Пост с id " + id + " удален";
    }
}
