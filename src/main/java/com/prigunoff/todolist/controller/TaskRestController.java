package com.prigunoff.todolist.controller;

import com.prigunoff.todolist.dto.TaskDto;
import com.prigunoff.todolist.dto.TaskTransformer;
import com.prigunoff.todolist.model.Task;
import com.prigunoff.todolist.service.StateService;
import com.prigunoff.todolist.service.TaskService;
import com.prigunoff.todolist.service.ToDoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks/")
public class TaskRestController {
    private TaskService taskService;
    private StateService stateService;
    private ToDoService toDoService;

    @Autowired
    public TaskRestController(TaskService taskService, StateService stateService, ToDoService toDoService) {
        this.taskService = taskService;
        this.stateService = stateService;
        this.toDoService = toDoService;
    }

    @GetMapping("")
    public ResponseEntity<List<Task>> getAll() {
        return new ResponseEntity<>(this.taskService.getAll(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<Task> getOneTask(@PathVariable("id") Long taskId) {
        if (taskId == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(taskService.readById(taskId), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or " +
            "@userServiceImpl.readById(@toDoServiceImpl.readById(#todo_id).owner.id).id == principal.getId()")// WORKS
    @PostMapping("/todo/{todo_id}")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity create(@PathVariable String todo_id, @RequestBody Map<String, String> task) {
        try {
            TaskDto taskDto = new TaskDto(Long.parseLong(task.get("task_id")), task.get("task"), task.get("priority"), Long.parseLong(task.get("todo_id")), Long.parseLong(task.get("state_id")));
            Task task1 = TaskTransformer.convertToEntity(taskDto,
                    toDoService.readById(taskDto.getTodoId()),
                    stateService.getByName("New"));
            return TaskTransformer.getTaskMap(taskService.create(task1), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @userServiceImpl.readById(@toDoServiceImpl.readById(@taskServiceImpl.readById(#id).id)) == principal.getId()")
    @PutMapping("{id}")
    public ResponseEntity<Task> update(@PathVariable String id, @RequestBody Map<String, String> editedMap) {
        try {
            TaskDto taskDto = new TaskDto(Long.parseLong(editedMap.get("task_id")), editedMap.get("task"), editedMap.get("priority"), Long.parseLong(editedMap.get("todo_id")), Long.parseLong(editedMap.get("state_id")));
            Task task = TaskTransformer.convertToEntity(
                    taskDto,
                    toDoService.readById(taskDto.getTodoId()),
                    stateService.readById(taskDto.getStateId())
            );
            Task oldTask = taskService.readById(Integer.valueOf(id));
            oldTask.setName(task.getName());
            oldTask.setState(task.getState());
            oldTask.setPriority(task.getPriority());
            taskService.update(oldTask);

            return TaskTransformer.getTaskMap(oldTask, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN') or @taskServiceImpl.readById(#id).todo.owner.id == principal.getId()")

    @DeleteMapping("{id}")
    public ResponseEntity<Task> delete(@PathVariable String id) {

        try {
            taskService.delete(Integer.parseInt(id));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
