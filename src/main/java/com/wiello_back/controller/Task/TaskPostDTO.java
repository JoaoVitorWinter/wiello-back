package com.wiello_back.controller.Task;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

public record TaskPostDTO(
        @NotEmpty
        @Length(max = 100)
        String title,
        @Length(max = 500)
        String description, Date deadline) {
}
