package com.prigunoff.todolist.controller;

import com.prigunoff.todolist.model.ToDo;
import com.prigunoff.todolist.service.ToDoService;
import com.prigunoff.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == authentication.principal.getId()")
    @GetMapping("{id}/all")
    public ResponseEntity<List<ToDo>> getAllTodosByUserId(@PathVariable("id") Long userId) {
        return new ResponseEntity<>(userService.readById(userId)
                .getMyTodos(), HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userId == principal.getId()") // WORKS!!!!
    @PostMapping("{id}")
    public ResponseEntity<ToDo> createToDoForUser(@PathVariable("id") Long userId, @RequestBody ToDo todo) {
        todo.setOwner(userService.readById(userId));
        todo.setCreatedAt(LocalDateTime.now());
        todo.setCollaborators(new ArrayList<>());
        toDoService.create(todo);

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
        return new ResponseEntity<>(todo, HttpStatus.OK);
    }
}
