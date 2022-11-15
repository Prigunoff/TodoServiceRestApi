package com.prigunoff.todolist.repository;

import com.prigunoff.todolist.model.Task;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository {
    @Query(value = "select * from tasks where todo_id = ?1", nativeQuery = true)
    List<Task> getByTodoId(long todoId);
}
