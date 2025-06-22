package com.wiello_back.entity;

import com.wiello_back.controller.ProjectColumn.ProjectColumnGetDTO;
import com.wiello_back.controller.Task.TaskSimpleGetDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
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
    @OneToMany(orphanRemoval = true, mappedBy = "column")
    private List<Task> tasks;

    public ProjectColumnGetDTO toProjectColumnGetDTO() {
        List<TaskSimpleGetDTO> tasks = this.getTasks().stream().map(Task::toTaskSimpleGetDTO).toList();
        return new ProjectColumnGetDTO(this.getId(), this.getName(), tasks);
    }
}
