package com.wiello_back.controller.Task;

import java.util.Date;
import java.util.UUID;

public record TaskFullGetDTO(UUID id, String title, String description, Date deadline, Date creationDate) {
}
