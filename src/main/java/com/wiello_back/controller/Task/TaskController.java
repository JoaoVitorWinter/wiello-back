package com.wiello_back.controller.Task;

import com.wiello_back.controller.Project.ProjectPatchDTO;
import com.wiello_back.controller.Project.ProjectPostDTO;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.service.ProjectService;
import com.wiello_back.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/task")
@AllArgsConstructor
public class TaskController {
    private TaskService taskService;
    private ProjectService projectService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{projectColumnID}")
    public ResponseEntity<?> createTask(@PathVariable UUID projectColumnID, @Valid @RequestBody TaskPostDTO taskPostDTO, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            taskService.createTask(projectColumnID, taskPostDTO, wielloUser);
            return ResponseEntity.status(201).build();
        } catch (DataIntegrityViolationException | ObjectNotFoundException exception) {
            return ResponseEntity.status(400).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{taskID}")
    public ResponseEntity<?> editTask(@PathVariable UUID taskID, @Valid @RequestBody TaskPutDTO taskPutDTO, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            taskService.editTask(taskID, taskPutDTO, wielloUser);
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
    @PatchMapping("/{taskID}/{projectColumnID}")
    public ResponseEntity<?> editTaskColumn(@PathVariable UUID taskID, @PathVariable UUID projectColumnID, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            taskService.editTaskColumn(taskID, projectColumnID, wielloUser);
            return ResponseEntity.status(200).build();
        } catch (DataIntegrityViolationException | BadRequestException exception) {
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
    @GetMapping("/{taskID}")
    public ResponseEntity<?> getTask(@PathVariable UUID taskID, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            return ResponseEntity.status(200).body(taskService.getTask(taskID, wielloUser));
        } catch (AccessDeniedException exception) {
            return ResponseEntity.status(403).build();
        } catch (ObjectNotFoundException exception) {
            return ResponseEntity.status(404).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{taskID}")
    public ResponseEntity<?> deleteTask(@PathVariable UUID taskID, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            taskService.deleteTask(taskID, wielloUser);
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
