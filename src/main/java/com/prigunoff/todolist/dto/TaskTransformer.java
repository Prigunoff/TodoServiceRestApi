package com.prigunoff.todolist.dto;

import com.prigunoff.todolist.model.Priority;
import com.prigunoff.todolist.model.State;
import com.prigunoff.todolist.model.Task;
import com.prigunoff.todolist.model.ToDo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;

public class TaskTransformer {
    public static TaskDto convertToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getName(),
                task.getPriority().toString(),
                task.getTodo().getId(),
                task.getState().getId()
        );
    }

    public static Task convertToEntity(TaskDto taskDto, ToDo todo, State state) {
        Task task = new Task();
        task.setId(taskDto.getId());
        task.setName(taskDto.getName());
        task.setPriority(Priority.valueOf(taskDto.getPriority()));
        task.setTodo(todo);
        task.setState(state);
        return task;
    }
    public static ResponseEntity getTaskMap(Task enteredTask, HttpStatus httpStatus) {
        Map<String, String> editedMap = new LinkedHashMap<>();
        editedMap.put("id", String.valueOf(enteredTask.getId()));
        editedMap.put("name", enteredTask.getName());
        editedMap.put("priority", enteredTask.getPriority().toString());
        editedMap.put("state", enteredTask.getState().getName());
        editedMap.put("todo_id", String.valueOf(enteredTask.getTodo().getId()));
        return new ResponseEntity(editedMap, httpStatus);
    }
}