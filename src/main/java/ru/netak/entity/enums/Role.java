package ru.netak.entity.enums;

public enum Role {
    USER,
    ADMIN;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
