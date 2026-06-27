package com.wiello_back.controller.Project;

import com.wiello_back.entity.WielloUser;
import com.wiello_back.service.ProjectService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/project")
@AllArgsConstructor
public class ProjectController {
    private ProjectService projectService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createProject(@Valid @RequestBody ProjectPostDTO projectPostDTO, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            projectService.createProject(projectPostDTO, wielloUser);
            return ResponseEntity.status(201).build();
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(400).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }
    
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{projectID}")
    public ResponseEntity<?> editProjectName(@PathVariable UUID projectID, @Valid @RequestBody ProjectPutDTO projectPutDTO, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            projectService.editProjectName(projectID, projectPutDTO, wielloUser);
            return ResponseEntity.status(200).build();
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(400).build();
        } catch (AccessDeniedException exception) {
            return ResponseEntity.status(403).build();
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(404).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllProjects(@AuthenticationPrincipal WielloUser wielloUser) {
        try {
            return ResponseEntity.status(200).body(projectService.getAllProjects(wielloUser));
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{projectID}")
    public ResponseEntity<?> getProject(@PathVariable UUID projectID, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            return ResponseEntity.status(200).body(projectService.getProject(projectID, wielloUser));
        } catch (AccessDeniedException exception) {
            return ResponseEntity.status(403).build();
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(404).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{projectID}")
    public ResponseEntity<?> deleteProject(@PathVariable UUID projectID, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            projectService.deleteProject(projectID, wielloUser);
            return ResponseEntity.status(200).build();
        } catch (AccessDeniedException exception) {
            return ResponseEntity.status(403).build();
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(404).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }
}
