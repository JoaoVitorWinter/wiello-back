package com.wiello_back.controller.WielloUser;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UserDTO(
        @NotEmpty
        @Length(min = 6, max = 50)
        String username,
        @NotEmpty
        @Email
        @Length(max = 100)
        String email,
        @NotEmpty
        @Length(min = 8, max = 50)
        String password) {
}
