package com.prigunoff.todolist.controller;

import com.prigunoff.todolist.model.User;
import com.prigunoff.todolist.service.RoleService;
import com.prigunoff.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRestController(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("{id}")
    public ResponseEntity<User> readUserById(@PathVariable("id") Long userId) {
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.readById(userId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.readById(2));
        userService.create(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #user.id == principal.getId()")
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.update(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {
        if (userService.readById(userId) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(userId);
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
    }

}
