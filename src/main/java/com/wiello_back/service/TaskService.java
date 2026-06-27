package com.wiello_back.service;

import com.wiello_back.controller.Task.TaskFullGetDTO;
import com.wiello_back.controller.Task.TaskPostDTO;
import com.wiello_back.controller.Task.TaskPutDTO;
import com.wiello_back.entity.ProjectColumn;
import com.wiello_back.entity.Task;
import com.wiello_back.entity.WielloUser;
import com.wiello_back.repository.ProjectColumnRepository;
import com.wiello_back.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;
    private ProjectColumnRepository projectColumnRepository;

    public void createTask(UUID projectColumnID, TaskPostDTO taskPostDTO, WielloUser wielloUser) {
        ProjectColumn projectColumn = projectColumnRepository.findById(projectColumnID).orElseThrow(() -> new ObjectNotFoundException(projectColumnID, "project column"));
        if (!projectColumn.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project!");
        }
        Task task = new Task();
        BeanUtils.copyProperties(taskPostDTO, task);
        task.setCreationDate(new Date());
        task.setColumn(projectColumn);
        task.setProject(projectColumn.getProject());
        taskRepository.save(task);
    }

    public TaskFullGetDTO getTask(UUID taskID, WielloUser wielloUser) {
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new ObjectNotFoundException(taskID, "task"));
        if (!task.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project!");
        }
        return task.toTaskFullGetDTO();
    }

    public void editTask(UUID taskID, TaskPutDTO taskPutDTO, WielloUser wielloUser) {
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new ObjectNotFoundException(taskID, "task"));
        if (!task.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        BeanUtils.copyProperties(taskPutDTO, task);
        taskRepository.save(task);
    }

    public void editTaskColumn(UUID taskID, UUID projectColumnID, WielloUser wielloUser) throws BadRequestException {
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new ObjectNotFoundException(taskID, "task"));
        if (!task.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        ProjectColumn projectColumn = projectColumnRepository.findById(projectColumnID).orElseThrow(() -> new ObjectNotFoundException(projectColumnID, "project column"));
        if (!task.getProject().equals(projectColumn.getProject())) {
            throw new BadRequestException("The column specified is not on the task's project");
        }
        task.setColumn(projectColumn);
        taskRepository.save(task);
    }

    public void deleteTask(UUID taskID, WielloUser wielloUser) {
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new ObjectNotFoundException(taskID, "task"));
        if (!task.getProject().getOwner().equals(wielloUser)) {
            throw new AccessDeniedException("You do not have access to this project");
        }
        taskRepository.deleteById(task.getId());
    }
}
