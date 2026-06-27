package com.wiello_back.service;

import com.wiello_back.controller.Project.ProjectFullGetDTO;
import com.wiello_back.controller.Project.ProjectPutDTO;
import com.wiello_back.controller.Project.ProjectPostDTO;
import com.wiello_back.controller.Project.ProjectSimpleGetDTO;
import com.wiello_back.entity.Project;
import com.wiello_back.entity.ProjectColumn;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.repository.ProjectRepository;
import jakarta.transaction.Transactional;
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
    private ProjectColumnService projectColumnService;

    @Transactional
    public void createProject(ProjectPostDTO projectPostDTO, WielloUser wielloUser) {
        Project project = new Project();
        BeanUtils.copyProperties(projectPostDTO, project);
        project.setCreationDate(new Date());
        project.setOwner(wielloUser);
        projectRepository.save(project);
        getInitialColumns().forEach((projectColumn) -> projectColumnService.createProjectColumn(project.getId(), projectColumn.getName(), wielloUser));
    }

    public List<ProjectColumn>  getInitialColumns() {
        return List.of(new ProjectColumn("To do"), new ProjectColumn("Doing"), new ProjectColumn("Done"));
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

    public void editProjectName(UUID projectID, ProjectPutDTO projectPutDTO, WielloUser wielloUser) {
        Project project = projectRepository.findById(projectID).orElseThrow(() -> new ObjectNotFoundException(projectID , "project"));
        if (!project.getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        project.setName(projectPutDTO.name());
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
