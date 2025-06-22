package com.wiello_back.controller.Project;

import com.wiello_back.controller.ProjectColumn.ProjectColumnGetDTO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public record ProjectFullGetDTO(UUID id, String name, Date creationDate, List<ProjectColumnGetDTO> columns) {
}
