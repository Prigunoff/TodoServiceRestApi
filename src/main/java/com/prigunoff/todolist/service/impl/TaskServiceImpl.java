package com.prigunoff.todolist.service.impl;

import com.prigunoff.todolist.exceptions.NullEntityReferenceException;
import com.prigunoff.todolist.model.Task;
import com.prigunoff.todolist.repository.TaskRepository;
import com.prigunoff.todolist.service.TaskService;

import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl implements TaskService {
    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task user) {
        return taskRepository.save(user);
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id).
                orElseThrow(() -> new NullEntityReferenceException("Task with id " + id + " not found"));
    }

    @Override
    public Task update(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void delete(long id) {
        Task task = readById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> tasks = taskRepository.getByTodoId(todoId);
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }
}
