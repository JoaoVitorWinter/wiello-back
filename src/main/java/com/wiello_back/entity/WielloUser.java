package com.wiello_back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WielloUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 50, nullable = false, unique = true)
    private String username;
    @JsonIgnore
    @ToString.Exclude
    @Column(nullable = false)
    private String password;
    @Column(length = 100, nullable = false)
    private String email;

    @Override
    public Collection<WielloUserRole> getAuthorities() {
        return List.of(WielloUserRole.STANDARD);
    }

    public void setPassword(String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        this.password = passwordEncoder.encode(password);
    }

}