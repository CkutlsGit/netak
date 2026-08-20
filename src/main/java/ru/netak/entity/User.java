package ru.netak.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.netak.entity.enums.Role;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@ToString(exclude = {"hashPassword", "posts"})
public class User {

    public User(String email, String username, Role role, String hashPassword) {
        this.email = email;
        this.username = username;
        this.role = role;
        this.hashPassword = hashPassword;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    @Column(nullable = false, length = 30, unique = true)
    private String username;

    @Column(nullable = false)
    private String hashPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;
}
