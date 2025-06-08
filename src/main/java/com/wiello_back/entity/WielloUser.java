package com.wiello_back.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class WielloUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 50, nullable = false, unique = true)
    private String username;
    @Column(length = 200, nullable = false)
    private String password;
    @Column(length = 100, nullable = false)
    private String email;

}