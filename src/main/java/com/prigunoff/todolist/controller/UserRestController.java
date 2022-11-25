package com.prigunoff.todolist.controller;

import com.prigunoff.todolist.model.User;
import com.prigunoff.todolist.service.RoleService;
import com.prigunoff.todolist.service.UserService;
import com.prigunoff.todolist.utils.LoggerColor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Slf4j
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

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userServiceImpl.readById(#userId).id == principal.getId()")
    @GetMapping("{id}")
    public ResponseEntity<User> readUserById(@PathVariable("id") Long userId) {
        if (userId == null) {
            log.error(LoggerColor.ERROR +"UserController: GET: readByUser: user with id: " + userId + " is null");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        log.info(LoggerColor.SUCCESS +"UserController: GET: readByUser : Success ! with id: " + userId);
        return new ResponseEntity<>(userService.readById(userId), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user == null) {
            log.error(LoggerColor.ERROR + "UserController:Post:CreateUser: User is null ");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(roleService.readById(2));
        userService.create(user);
        log.info(LoggerColor.SUCCESS +"UserController:POST:createUser: User -> created with id " + user.getId());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or #user.id == principal.getId()")
    @PutMapping("")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.update(user);
        log.info(LoggerColor.SUCCESS +"UserController:PUT:updateUser: User -> Updated with id "  + user.getId());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userServiceImpl.readById(#userId).id == principal.getId()")
    @DeleteMapping("{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long userId) {
        if (userService.readById(userId) == null) {
            log.error(LoggerColor.ERROR +"UserController:Delete:deleteUser:User ->Not Found with email "
                    + userService.readById(userId).getEmail());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.delete(userId);
        log.info(LoggerColor.SUCCESS +"UserController:Delete:deleteUser:User - > Deleted with id " + userId);
        return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info(LoggerColor.SUCCESS + "UserController:GET:getAllUsers: Success at " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        return new ResponseEntity<>(this.userService.getAll(), HttpStatus.OK);
    }

}
