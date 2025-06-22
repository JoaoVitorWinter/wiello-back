package com.wiello_back.service;

import com.wiello_back.controller.Project.ProjectFullGetDTO;
import com.wiello_back.controller.Project.ProjectPatchDTO;
import com.wiello_back.controller.Project.ProjectPostDTO;
import com.wiello_back.controller.Project.ProjectSimpleGetDTO;
import com.wiello_back.entity.Project;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProjectService {
    private ProjectRepository projectRepository;

    public void createProject(ProjectPostDTO projectPostDTO, WielloUser wielloUser) {
        Project project = new Project();
        BeanUtils.copyProperties(projectPostDTO, project);
        project.setCreationDate(new Date());
        project.setOwner(wielloUser);
        System.out.println(new Date());
        projectRepository.save(project);
    }

    public List<ProjectSimpleGetDTO> getAllProjects(WielloUser wielloUser) {
        List<Project> projects = projectRepository.findAllByOwnerOrderByCreationDateDesc(wielloUser);
        return projects.stream().map(Project::toProjectSimpleGetDTO).toList();
    }

    public ProjectFullGetDTO getProject(UUID projectID, WielloUser wielloUser) {
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new ObjectNotFoundException(projectID, "project"));
        if (!project.getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project!");
        }
        return project.toProjectFullGetDTO();
    }

    public void editProjectName(ProjectPatchDTO projectPatchDTO, WielloUser wielloUser) {
        UUID id = UUID.fromString(projectPatchDTO.id());
        Project project = projectRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(id, "project"));
        if (!project.getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        project.setName(projectPatchDTO.name());
        projectRepository.save(project);
    }

    public void deleteProject(UUID projectID, WielloUser wielloUser) {
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new ObjectNotFoundException(projectID, "project"));
        if (!project.getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        projectRepository.deleteById(projectID);
    }
}
