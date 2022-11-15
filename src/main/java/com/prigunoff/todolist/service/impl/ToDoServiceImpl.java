package com.prigunoff.todolist.service.impl;

import com.prigunoff.todolist.exceptions.NullEntityReferenceException;
import com.prigunoff.todolist.model.ToDo;
import com.prigunoff.todolist.repository.ToDoRepository;
import com.prigunoff.todolist.service.ToDoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {
    private ToDoRepository todoRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ToDo create(ToDo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new NullEntityReferenceException("ToDo with id " + id + " not found"));
    }

    @Override
    public ToDo update(ToDo todo) {
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long id) {
        ToDo todo = readById(id);
        todoRepository.delete(todo);
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> todos = todoRepository.findAll();
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = todoRepository.getByUserId(userId);
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

}
