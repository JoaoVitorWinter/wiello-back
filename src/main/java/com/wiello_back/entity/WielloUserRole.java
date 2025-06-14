package com.wiello_back.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@AllArgsConstructor
public enum WielloUserRole implements GrantedAuthority {
    STANDARD("Standard");

    private final String name;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
