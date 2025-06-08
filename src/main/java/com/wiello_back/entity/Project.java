package com.wiello_back.entity;

import jakarta.persistence.*;
import jakarta.persistence.Column;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(nullable = false)
    private Date creationDate;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private WielloUser owner;
}
