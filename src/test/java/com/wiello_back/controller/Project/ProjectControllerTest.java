package com.wiello_back.controller.Project;

import com.wiello_back.entity.WielloUser;
import com.wiello_back.service.ProjectService;
import org.hibernate.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProjectController Tests")
class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    private WielloUser wielloUser;
    private ProjectPostDTO projectPostDTO;
    private ProjectPutDTO projectPutDTO;
    private UUID projectID;

    @BeforeEach
    void setUp() {
        wielloUser = new WielloUser();
        wielloUser.setId(UUID.randomUUID());
        wielloUser.setEmail("test@example.com");

        projectID = UUID.randomUUID();

        projectPostDTO = new ProjectPostDTO("Test Project");
        projectPutDTO = new ProjectPutDTO("Updated Project");
    }

    @Test
    @DisplayName("Should create project successfully")
    void testCreateProjectSuccess() {
        ResponseEntity<?> response = projectController.createProject(projectPostDTO, wielloUser);

        assertEquals(201, response.getStatusCode().value());
        verify(projectService, times(1)).createProject(projectPostDTO, wielloUser);
    }

    @Test
    @DisplayName("Should return 400 when creating project with duplicate name")
    void testCreateProjectWithDataIntegrityViolation() {
        doThrow(new DataIntegrityViolationException("Duplicate project"))
                .when(projectService).createProject(any(ProjectPostDTO.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.createProject(projectPostDTO, wielloUser);

        assertEquals(400, response.getStatusCode().value());
        verify(projectService, times(1)).createProject(projectPostDTO, wielloUser);
    }

    @Test
    @DisplayName("Should return 500 when unexpected error occurs during project creation")
    void testCreateProjectWithUnexpectedError() {
        doThrow(new RuntimeException("Unexpected error"))
                .when(projectService).createProject(any(ProjectPostDTO.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.createProject(projectPostDTO, wielloUser);

        assertEquals(500, response.getStatusCode().value());
        verify(projectService, times(1)).createProject(projectPostDTO, wielloUser);
    }

    @Test
    @DisplayName("Should edit project name successfully")
    void testEditProjectNameSuccess() {
        ResponseEntity<?> response = projectController.editProjectName(projectID, projectPutDTO, wielloUser);

        assertEquals(200, response.getStatusCode().value());
        verify(projectService, times(1)).editProjectName(projectID, projectPutDTO, wielloUser);
    }

    @Test
    @DisplayName("Should return 400 when editing project with invalid data")
    void testEditProjectNameWithDataIntegrityViolation() {
        doThrow(new DataIntegrityViolationException("Invalid data"))
                .when(projectService).editProjectName(any(UUID.class), any(ProjectPutDTO.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.editProjectName(projectID, projectPutDTO, wielloUser);

        assertEquals(400, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 403 when user has no permission to edit project")
    void testEditProjectNameWithAccessDenied() {
        doThrow(new AccessDeniedException("You do not have access to this project"))
                .when(projectService).editProjectName(any(UUID.class), any(ProjectPutDTO.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.editProjectName(projectID, projectPutDTO, wielloUser);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 404 when project not found for edit")
    void testEditProjectNameNotFound() {
        doThrow(new ObjectNotFoundException(projectID, "project"))
                .when(projectService).editProjectName(any(UUID.class), any(ProjectPutDTO.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.editProjectName(projectID, projectPutDTO, wielloUser);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 500 when unexpected error occurs during edit")
    void testEditProjectNameWithUnexpectedError() {
        doThrow(new RuntimeException("Unexpected error"))
                .when(projectService).editProjectName(any(UUID.class), any(ProjectPutDTO.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.editProjectName(projectID, projectPutDTO, wielloUser);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should get all projects successfully")
    void testGetAllProjectsSuccess() {
        List<ProjectSimpleGetDTO> projectList = new ArrayList<>();
        when(projectService.getAllProjects(wielloUser)).thenReturn(projectList);

        ResponseEntity<?> response = projectController.getAllProjects(wielloUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(projectList, response.getBody());
        verify(projectService, times(1)).getAllProjects(wielloUser);
    }

    @Test
    @DisplayName("Should return 500 when error occurs getting all projects")
    void testGetAllProjectsWithError() {
        doThrow(new RuntimeException("Database error"))
                .when(projectService).getAllProjects(any(WielloUser.class));

        ResponseEntity<?> response = projectController.getAllProjects(wielloUser);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should get project successfully")
    void testGetProjectSuccess() {
        ProjectFullGetDTO projectDTO = new ProjectFullGetDTO(
                java.util.UUID.randomUUID(),
                "Projeto Teste",
                new java.util.Date(),
                java.util.List.of());
        when(projectService.getProject(projectID, wielloUser)).thenReturn(projectDTO);

        ResponseEntity<?> response = projectController.getProject(projectID, wielloUser);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(projectDTO, response.getBody());
        verify(projectService, times(1)).getProject(projectID, wielloUser);
    }

    @Test
    @DisplayName("Should return 403 when user has no permission to access project")
    void testGetProjectAccessDenied() {
        doThrow(new AccessDeniedException("You do not have access to this project"))
                .when(projectService).getProject(any(UUID.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.getProject(projectID, wielloUser);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 404 when project not found")
    void testGetProjectNotFound() {
        doThrow(new ObjectNotFoundException(projectID, "project"))
                .when(projectService).getProject(any(UUID.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.getProject(projectID, wielloUser);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 500 when error occurs getting project")
    void testGetProjectWithError() {
        doThrow(new RuntimeException("Database error"))
                .when(projectService).getProject(any(UUID.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.getProject(projectID, wielloUser);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should delete project successfully")
    void testDeleteProjectSuccess() {
        ResponseEntity<?> response = projectController.deleteProject(projectID, wielloUser);

        assertEquals(200, response.getStatusCode().value());
        verify(projectService, times(1)).deleteProject(projectID, wielloUser);
    }

    @Test
    @DisplayName("Should return 403 when user has no permission to delete project")
    void testDeleteProjectAccessDenied() {
        doThrow(new AccessDeniedException("You do not have access to this project"))
                .when(projectService).deleteProject(any(UUID.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.deleteProject(projectID, wielloUser);

        assertEquals(403, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 404 when project not found for deletion")
    void testDeleteProjectNotFound() {
        doThrow(new ObjectNotFoundException(projectID, "project"))
                .when(projectService).deleteProject(any(UUID.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.deleteProject(projectID, wielloUser);

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    @DisplayName("Should return 500 when error occurs during deletion")
    void testDeleteProjectWithError() {
        doThrow(new RuntimeException("Database error"))
                .when(projectService).deleteProject(any(UUID.class), any(WielloUser.class));

        ResponseEntity<?> response = projectController.deleteProject(projectID, wielloUser);

        assertEquals(500, response.getStatusCode().value());
    }
}
