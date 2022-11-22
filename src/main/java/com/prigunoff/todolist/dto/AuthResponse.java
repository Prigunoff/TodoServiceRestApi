package com.prigunoff.todolist.dto;

import lombok.Data;

@Data
public class AuthResponse {

    private String username;

    private String token;
}
