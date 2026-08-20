package ru.netak.security;

import lombok.Getter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.netak.entity.User;
import ru.netak.entity.enums.Role;

import java.util.Collection;
import java.util.Collections;

@Getter
public class SecurityUser implements UserDetails {

    public SecurityUser(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.hashPassword = user.getHashPassword();
        this.role = user.getRole();
    }

    private long id;
    private String email;
    private String username;
    private String hashPassword;
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton((GrantedAuthority) (() -> role.getAuthority()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public @Nullable String getPassword() {
        return hashPassword;
    }
}
