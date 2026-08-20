package ru.netak.dto.post;

import ru.netak.dto.user.UserDto;
import ru.netak.entity.Post;

public record PostDto(
        long id,
        UserDto user,
        String title,
        String description
) {
    public static PostDto fromEntity(Post post) {
        return new PostDto(
                post.getId(),
                new UserDto(
                        post.getUser().getId(),
                        post.getUser().getUsername()
                ),
                post.getTitle(),
                post.getDescription()
        );
    }
}
