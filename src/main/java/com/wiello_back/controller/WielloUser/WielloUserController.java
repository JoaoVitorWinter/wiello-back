package com.wiello_back.controller.WielloUser;

import com.wiello_back.exception.UserAlreadyExistsException;
import com.wiello_back.service.WielloUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
@Tag(name = "Usuário", description = "Cadastro e autenticação de usuários")
public class WielloUserController {
    private WielloUserService wielloUserService;

    @Operation(summary = "Login", description = "Autentica o usuário e retorna o token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token JWT retornado"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginDTO loginDTO) {
        try {
            return ResponseEntity.status(200).body(wielloUserService.login(loginDTO.username(), loginDTO.password()));
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(400).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @Operation(summary = "Teste de autenticação", description = "Verifica se o token JWT é válido")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticado"),
            @ApiResponse(responseCode = "403", description = "Não autenticado")
    })
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/teste")
    public String authenticationTest() {
        return "Got in";
    }

    @Operation(summary = "Criar usuário", description = "Cadastra um novo usuário")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário criado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou usuário já existe"),
            @ApiResponse(responseCode = "500", description = "Erro interno")
    })
    @PreAuthorize("permitAll()")
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO) {
        try {
            wielloUserService.createUser(userDTO);
            return ResponseEntity.status(201).build();
        } catch(UserAlreadyExistsException exception) {
            return ResponseEntity.status(400).body(exception.getMessage());
        } catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(400).build();
        } catch (Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }


}
