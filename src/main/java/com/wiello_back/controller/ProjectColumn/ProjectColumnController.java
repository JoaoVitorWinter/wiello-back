package com.wiello_back.controller.ProjectColumn;

import com.wiello_back.controller.Project.ProjectPostDTO;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.service.ProjectColumnService;
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
@RequestMapping("/column")
@AllArgsConstructor
@Tag(name = "Coluna", description = "Gerenciamento de colunas de projeto")
public class ProjectColumnController {
    private ProjectColumnService projectColumnService;

    @Operation(summary = "Criar coluna", description = "Cria uma nova coluna no projeto informado")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Coluna criada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Projeto não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{projectID}")
    public ResponseEntity<?> createProjectColumn(@PathVariable UUID projectID, @Valid @RequestBody ProjectColumnPostDTO projectColumnPostDTO, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            projectColumnService.createProjectColumn(projectID, projectColumnPostDTO.name(), wielloUser);
            return ResponseEntity.status(201).build();
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

    @Operation(summary = "Editar nome da coluna")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coluna atualizada"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Coluna não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{projectColumnID}")
    public ResponseEntity<?> editProjectColumnName(@PathVariable UUID projectColumnID, @Valid @RequestBody ProjectColumnPatchDTO projectColumnPatchDTO, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            projectColumnService.editProjectColumnName(projectColumnID, projectColumnPatchDTO, wielloUser);
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

    @Operation(summary = "Deletar coluna")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Coluna deletada"),
            @ApiResponse(responseCode = "403", description = "Sem permissão"),
            @ApiResponse(responseCode = "404", description = "Coluna não encontrada"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{projectColumnID}")
    public ResponseEntity<?> deleteProjectColumn(@PathVariable UUID projectColumnID, @AuthenticationPrincipal WielloUser wielloUser) {
        try {
            projectColumnService.deleteProjectColumn(projectColumnID, wielloUser);
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
