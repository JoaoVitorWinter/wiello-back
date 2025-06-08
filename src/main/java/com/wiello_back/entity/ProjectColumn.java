package com.wiello_back.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class ProjectColumn {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 30, nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
