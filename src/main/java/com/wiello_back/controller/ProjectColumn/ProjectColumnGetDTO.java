package com.wiello_back.controller.ProjectColumn;

import com.wiello_back.controller.Task.TaskSimpleGetDTO;

import java.util.List;
import java.util.UUID;

public record ProjectColumnGetDTO(UUID id, String name, List<TaskSimpleGetDTO> tasks) {
}
