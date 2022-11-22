package com.prigunoff.todolist.controller;

import com.prigunoff.todolist.dto.AuthRequest;
import com.prigunoff.todolist.dto.AuthResponse;
import com.prigunoff.todolist.model.User;
import com.prigunoff.todolist.security.jwt.JwtProvider;
import com.prigunoff.todolist.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthRestController {

    private AuthenticationManager authenticate;
    private JwtProvider jwtProvider;
    private UserService userService;

    @Autowired
    public JwtAuthRestController(AuthenticationManager authenticate, JwtProvider jwtProvider, UserService userService) {
        this.authenticate = authenticate;
        this.jwtProvider = jwtProvider;
        this.userService = userService;
    }


    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity login(@RequestBody AuthRequest request) {
        try {
            String username = request.getUsername();
            authenticate.authenticate(new UsernamePasswordAuthenticationToken(username, request.getPassword()));
            Optional<User> users = Optional.ofNullable(userService.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("Invalid username.")));
            String token = jwtProvider.createToken(username, users.get().getRole());
            AuthResponse responseData = new AuthResponse();
            responseData.setUsername(username);
            responseData.setToken(token);

            return ResponseEntity.ok(responseData);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid data.");
        }
    }

}
