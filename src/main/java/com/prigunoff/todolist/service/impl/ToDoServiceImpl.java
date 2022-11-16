package com.prigunoff.todolist.service.impl;

import com.prigunoff.todolist.exceptions.NullEntityReferenceException;
import com.prigunoff.todolist.model.ToDo;
import com.prigunoff.todolist.repository.ToDoRepository;
import com.prigunoff.todolist.service.ToDoService;
import com.prigunoff.todolist.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {
    private ToDoRepository todoRepository;
    private UserService userService;
    public ToDoServiceImpl(ToDoRepository todoRepository,UserService userService) {
        this.todoRepository = todoRepository;
        this.userService = userService;
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
        if (todo != null) {
            if (todo.getOwner() == null) {
                todo.setOwner(readById(todo.getId()).getOwner());
            }
            todo.setCreatedAt(readById(todo.getId()).getCreatedAt());
            return todoRepository.save(todo);
        }
        throw new NullEntityReferenceException("Todo cannot be 'null'");
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
