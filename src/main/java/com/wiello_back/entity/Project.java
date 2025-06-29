package com.wiello_back.entity;

import com.wiello_back.controller.Project.ProjectFullGetDTO;
import com.wiello_back.controller.Project.ProjectSimpleGetDTO;
import com.wiello_back.controller.ProjectColumn.ProjectColumnGetDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Data
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;
    @Column(length = 50, nullable = false)
    private String name;
    @Column(nullable = false)
    private Date creationDate;
    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private WielloUser owner;
    @OneToMany(orphanRemoval = true, mappedBy = "project", fetch = FetchType.LAZY)
    private List<Task> tasks;
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true, mappedBy = "project", fetch = FetchType.LAZY)
    private List<ProjectColumn> columns;

    public ProjectSimpleGetDTO toProjectSimpleGetDTO() {
        return new ProjectSimpleGetDTO(this.getId(), this.getName());
    }

    public ProjectFullGetDTO toProjectFullGetDTO() {
        List<ProjectColumnGetDTO> columns = this.getColumns().stream().map(ProjectColumn::toProjectColumnGetDTO).toList();
        return new ProjectFullGetDTO(this.getId(), this.getName(), this.getCreationDate(), columns);
    }
}
