package com.wiello_back.controller.Project;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;


public record ProjectPutDTO(
        @NotEmpty
        @Length(max = 50)
        String name) {
}
