package com.wiello_back.controller.Task;

import com.wiello_back.entity.WielloUser;
import com.wiello_back.service.ProjectService;
import com.wiello_back.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tarefa", description = "Gerenciamento de tarefas")
public class TaskController {
    private TaskService taskService;
    private ProjectService projectService;

    @Operation(summary = "Criar tarefa", description = "Cria uma nova tarefa na coluna informada")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tarefa criada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou coluna não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(summary = "Editar tarefa", description = "Atualiza título, descrição e prazo da tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(summary = "Mover tarefa", description = "Move a tarefa para outra coluna do mesmo projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa movida"),
            @ApiResponse(responseCode = "400", description = "Coluna de destino inválida"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Tarefa ou coluna não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(summary = "Buscar tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa encontrada"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(summary = "Deletar tarefa")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tarefa deletada"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Tarefa não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
