package com.wiello_back.service;

import com.wiello_back.controller.ProjectColumn.ProjectColumnPatchDTO;
import com.wiello_back.entity.Project;
import com.wiello_back.entity.ProjectColumn;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.repository.ProjectColumnRepository;
import com.wiello_back.repository.ProjectRepository;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectColumnService Tests")
class ProjectColumnServiceTest {

    @Mock
    private ProjectColumnRepository projectColumnRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectColumnService projectColumnService;

    private WielloUser owner;
    private WielloUser otherUser;
    private Project project;
    private ProjectColumn projectColumn;
    private UUID projectID;
    private UUID projectColumnID;

    @BeforeEach
    void setUp() {
        owner = new WielloUser();
        owner.setId(UUID.randomUUID());
        owner.setEmail("owner@example.com");

        otherUser = new WielloUser();
        otherUser.setId(UUID.randomUUID());
        otherUser.setEmail("other@example.com");

        projectID = UUID.randomUUID();
        projectColumnID = UUID.randomUUID();

        project = new Project();
        project.setId(projectID);
        project.setName("Test Project");
        project.setOwner(owner);

        projectColumn = new ProjectColumn();
        projectColumn.setId(projectColumnID);
        projectColumn.setName("Test Column");
        projectColumn.setProject(project);
    }

    @Test
    @DisplayName("Should create project column successfully")
    void testCreateProjectColumnSuccess() {
        when(projectRepository.findById(projectID)).thenReturn(Optional.of(project));

        projectColumnService.createProjectColumn(projectID, "New Column", owner);

        verify(projectRepository, times(1)).findById(projectID);
        verify(projectColumnRepository, times(1)).save(any(ProjectColumn.class));
    }

    @Test
    @DisplayName("Should throw exception when project not found during creation")
    void testCreateProjectColumnProjectNotFound() {
        when(projectRepository.findById(projectID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> projectColumnService.createProjectColumn(projectID, "New Column", owner));

        verify(projectRepository, times(1)).findById(projectID);
        verify(projectColumnRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user is not project owner")
    void testCreateProjectColumnAccessDenied() {
        when(projectRepository.findById(projectID)).thenReturn(Optional.of(project));

        assertThrows(AccessDeniedException.class,
                () -> projectColumnService.createProjectColumn(projectID, "New Column", otherUser));

        verify(projectRepository, times(1)).findById(projectID);
        verify(projectColumnRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should edit project column name successfully")
    void testEditProjectColumnNameSuccess() {
        ProjectColumnPatchDTO patchDTO = new ProjectColumnPatchDTO("Updated Column");
        when(projectColumnRepository.findById(projectColumnID)).thenReturn(Optional.of(projectColumn));

        projectColumnService.editProjectColumnName(projectColumnID, patchDTO, owner);

        verify(projectColumnRepository, times(1)).findById(projectColumnID);
        verify(projectColumnRepository, times(1)).save(projectColumn);
        assertEquals("Updated Column", projectColumn.getName());
    }

    @Test
    @DisplayName("Should throw exception when project column not found during edit")
    void testEditProjectColumnNameNotFound() {
        ProjectColumnPatchDTO patchDTO = new ProjectColumnPatchDTO("Updated Column");
        when(projectColumnRepository.findById(projectColumnID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> projectColumnService.editProjectColumnName(projectColumnID, patchDTO, owner));

        verify(projectColumnRepository, times(1)).findById(projectColumnID);
        verify(projectColumnRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user is not project owner during edit")
    void testEditProjectColumnNameAccessDenied() {
        ProjectColumnPatchDTO patchDTO = new ProjectColumnPatchDTO("Updated Column");
        when(projectColumnRepository.findById(projectColumnID)).thenReturn(Optional.of(projectColumn));

        assertThrows(AccessDeniedException.class,
                () -> projectColumnService.editProjectColumnName(projectColumnID, patchDTO, otherUser));

        verify(projectColumnRepository, times(1)).findById(projectColumnID);
        verify(projectColumnRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete project column successfully")
    void testDeleteProjectColumnSuccess() {
        when(projectColumnRepository.findById(projectColumnID)).thenReturn(Optional.of(projectColumn));

        projectColumnService.deleteProjectColumn(projectColumnID, owner);

        verify(projectColumnRepository, times(1)).findById(projectColumnID);
        verify(projectColumnRepository, times(1)).deleteById(projectColumnID);
    }

    @Test
    @DisplayName("Should throw exception when project column not found during deletion")
    void testDeleteProjectColumnNotFound() {
        when(projectColumnRepository.findById(projectColumnID)).thenReturn(Optional.empty());

        assertThrows(ObjectNotFoundException.class,
                () -> projectColumnService.deleteProjectColumn(projectColumnID, owner));

        verify(projectColumnRepository, times(1)).findById(projectColumnID);
        verify(projectColumnRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should throw AccessDeniedException when user is not project owner during deletion")
    void testDeleteProjectColumnAccessDenied() {
        when(projectColumnRepository.findById(projectColumnID)).thenReturn(Optional.of(projectColumn));

        assertThrows(AccessDeniedException.class,
                () -> projectColumnService.deleteProjectColumn(projectColumnID, otherUser));

        verify(projectColumnRepository, times(1)).findById(projectColumnID);
        verify(projectColumnRepository, never()).deleteById(any());
    }
}
