package ru.netak.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netak.dto.post.PostDto;
import ru.netak.dto.post.PostFormDto;
import ru.netak.service.PostService;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        return ResponseEntity.ok().body(postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable long id) {
        return ResponseEntity.ok().body(postService.getPostById(id));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<List<PostDto>> getPostsUserById(@PathVariable long id) {
        return ResponseEntity.ok().body(postService.getPostsUserById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody @Valid PostFormDto createData) {
        return ResponseEntity.ok().body(postService.createPost(createData));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable long id) {
        return ResponseEntity.ok().body(postService.deletePost(id));
    }
}
