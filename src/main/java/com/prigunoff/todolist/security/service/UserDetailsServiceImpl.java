package com.prigunoff.todolist.security.service;

import com.prigunoff.todolist.model.User;
import com.prigunoff.todolist.repository.UserRepository;
import com.prigunoff.todolist.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) {
        Optional<User> user = userRepository.findByEmail(s);
        if (user == null)
            throw new UsernameNotFoundException("User not found!");
        return new UserDetailsImpl(user.get());
    }
}
