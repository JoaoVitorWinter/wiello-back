package com.wiello_back.controller.Project;

import com.wiello_back.entity.WielloUser;
import com.wiello_back.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Projeto", description = "Gerenciamento de projetos")
public class ProjectController {
    private ProjectService projectService;

    @Operation(summary = "Criar projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Projeto criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
    
    @Operation(summary = "Editar nome do projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(summary = "Listar projetos", description = "Retorna todos os projetos do usuário autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de projetos"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<?> getAllProjects(@AuthenticationPrincipal WielloUser wielloUser) {
        try {
            return ResponseEntity.status(200).body(projectService.getAllProjects(wielloUser));
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Buscar projeto", description = "Retorna o projeto com suas colunas e tarefas")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto encontrado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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

    @Operation(summary = "Deletar projeto")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Projeto deletado"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
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
