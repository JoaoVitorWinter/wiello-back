package com.wiello_back.service;

import com.wiello_back.controller.ProjectColumn.ProjectColumnPatchDTO;
import com.wiello_back.controller.ProjectColumn.ProjectColumnPostDTO;
import com.wiello_back.entity.Project;
import com.wiello_back.entity.ProjectColumn;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.repository.ProjectColumnRepository;
import com.wiello_back.repository.ProjectRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectColumnService {
    private ProjectColumnRepository projectColumnRepository;
    private ProjectRepository projectRepository;

    public void createProjectColumn(UUID projectID, String name, WielloUser wielloUser) {
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new ObjectNotFoundException(projectID, "project"));
        if (!project.getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        ProjectColumn projectColumn = new ProjectColumn();
        projectColumn.setName(name);
        projectColumn.setProject(project);
        projectColumnRepository.save(projectColumn);
    }

    public void editProjectColumnName(UUID projectColumnID, ProjectColumnPatchDTO projectColumnPatchDTO, WielloUser wielloUser) {
        ProjectColumn projectColumn = projectColumnRepository.findById(projectColumnID).orElseThrow(() -> new ObjectNotFoundException(projectColumnID, "project column"));
        if (!projectColumn.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        projectColumn.setName(projectColumnPatchDTO.name());
        projectColumnRepository.save(projectColumn);
    }

    public void deleteProjectColumn(UUID projectColumnID, WielloUser wielloUser) {
        ProjectColumn projectColumn = projectColumnRepository.findById(projectColumnID).orElseThrow(() -> new ObjectNotFoundException(projectColumnID, "project column"));
        if (!projectColumn.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        projectColumnRepository.deleteById(projectColumn.getId());
    }
}
