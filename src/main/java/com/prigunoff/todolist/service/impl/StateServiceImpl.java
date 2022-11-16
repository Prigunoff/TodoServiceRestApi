package com.prigunoff.todolist.service.impl;

import com.prigunoff.todolist.exceptions.NullEntityReferenceException;
import com.prigunoff.todolist.model.State;
import com.prigunoff.todolist.repository.StateRepository;
import com.prigunoff.todolist.service.StateService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class StateServiceImpl implements StateService {
    private StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State create(State state) {
        return stateRepository.save(state);
    }

    @Override
    public State readById(long id) {
        return stateRepository.findById(id)
                .orElseThrow(() -> new NullEntityReferenceException("State with id " + id + " not found"));
    }

    @Override
    public State update(State state) {
        return stateRepository.save(state);
    }

    @Override
    public void delete(long id) {
        State state = readById(id);
        stateRepository.delete(state);
    }

    @Override
    public State getByName(String name) {
        Optional<State> optional = Optional.ofNullable(stateRepository.getByName(name));
        return optional.get();
    }

    @Override
    public List<State> getAll() {
        List<State> states = stateRepository.getAll();
        return states.isEmpty() ? new ArrayList<>() : states;
    }
}
