package ru.netak.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.netak.entity.Post;
import ru.netak.entity.User;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Override
    @EntityGraph(attributePaths = {"user"})
    List<Post> findAll();

    Optional<Post> findPostById(long id);

    @EntityGraph(attributePaths = {"user"})
    List<Post> findPostByUserId(long id );
}
