package com.wiello_back.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 100, nullable = false)
    private String title;
    @Column(length = 500)
    private String description;
    private Date deadline;
    @Column(nullable = false)
    private Date creationDate;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
    @ManyToOne
    @JoinColumn(name = "column_id", nullable = false)
    private ProjectColumn column;
}
