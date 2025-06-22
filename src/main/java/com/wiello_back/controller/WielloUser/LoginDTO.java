package com.wiello_back.controller.WielloUser;

import jakarta.validation.constraints.NotEmpty;

public record LoginDTO(
        @NotEmpty
        String username,
        @NotEmpty
        String password) {
}
