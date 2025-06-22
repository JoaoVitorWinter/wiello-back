package com.wiello_back.controller.WielloUser;

import com.wiello_back.exception.UserAlreadyExistsException;
import com.wiello_back.service.WielloUserService;
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
public class WielloUserController {
    private WielloUserService wielloUserService;

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

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/teste")
    public String authenticationTest() {
        return "Got in";
    }

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
