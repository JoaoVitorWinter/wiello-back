package com.wiello_back.controller.ProjectColumn;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record ProjectColumnPostDTO(
        @NotEmpty
        @Length(max = 30)
        String name) {
}
