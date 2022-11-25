package com.prigunoff.todolist.controller;

import com.prigunoff.todolist.model.ToDo;
import com.prigunoff.todolist.service.ToDoService;
import com.prigunoff.todolist.service.UserService;
import com.prigunoff.todolist.utils.LoggerColor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/todos/")
public class ToDoRestController {
    private ToDoService toDoService;
    private UserService userService;

    @Autowired
    public ToDoRestController(ToDoService toDoService, UserService userService) {
        this.toDoService = toDoService;
        this.userService = userService;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("{id}")
    public ResponseEntity<ToDo> getToDo(@PathVariable("id") Long todoId) {
        if (todoId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(this.toDoService.readById(todoId), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    @GetMapping("")
    public ResponseEntity<List<ToDo>> getAllTodos() {
        return new ResponseEntity<>(toDoService.getAll(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.getId()")
    @GetMapping("{id}/all")
    public ResponseEntity<List<ToDo>> getAllTodosByUserId(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.readById(userId)
                .getMyTodos(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.getId()")
    @PostMapping("{id}")
    public ResponseEntity<ToDo> createToDoForUser(@PathVariable("id") Long userId, @RequestBody ToDo todo) {
        todo.setOwner(userService.readById(userId));
        todo.setCreatedAt(LocalDateTime.now());
        todo.setCollaborators(new ArrayList<>());
        toDoService.create(todo);
        log.info(LoggerColor.SUCCESS + "ToDoController:POST:Created ToDo with title " + todo.getTitle());
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') " +
            "or " +
            "@userServiceImpl.readById(@toDoServiceImpl.readById(#id).owner.id).id == principal.getId()")
    @DeleteMapping("{id}")
    public ResponseEntity<ToDo> deleteToDo(@PathVariable("id") Long id) {
        if (toDoService.readById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        log.info(LoggerColor.SUCCESS + "ToDoController:DELETE:Deleting todo with title " + toDoService.readById(id).getTitle());
        toDoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostAuthorize("hasRole('ROLE_ADMIN') or #todo.getOwner().getId() == principal.getId()")
    @PutMapping("")
    public ResponseEntity<ToDo> updateToDo(@RequestBody ToDo todo) {
        if (todo == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (todo.getCollaborators() == null) {
            todo.setCollaborators(new ArrayList<>());
        } else {
            todo.setCollaborators(todo.getCollaborators());
        }
        toDoService.update(todo);
        log.info(LoggerColor.SUCCESS + "ToDoController:PUT:UpdateTodo: ToDo updated with id "  + toDoService.readById(todo.getId()));
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }
}
