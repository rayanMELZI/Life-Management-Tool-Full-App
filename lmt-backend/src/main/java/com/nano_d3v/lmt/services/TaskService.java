package com.nano_d3v.lmt.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nano_d3v.lmt.api.models.Task;
import com.nano_d3v.lmt.api.models.Task.Importance;
import com.nano_d3v.lmt.api.models.Task.Urgency;
import com.nano_d3v.lmt.api.repositories.TaskRepository;

@Service
public class TaskService {

    public final TaskRepository taskRepository;
    public final DomainColumnService domainColumnService;

    public TaskService(TaskRepository taskRepository, DomainColumnService domainColumnService) {
        this.taskRepository = taskRepository;
        this.domainColumnService = domainColumnService;
    }

    //get a Column Tasks By ColumnId
    public List<Task> getColumnTasksByColumnId(Integer columnId){
        return taskRepository.findByColumnId(columnId);
    }

    //add a new Task to one of the user's columns
    public Task addTask(String content, Importance importance, Urgency urgency, Integer columnId, Integer userId){
        domainColumnService.getOwnedColumn(columnId, userId);
        Task newTask = new Task(content, importance, urgency, columnId);
        return taskRepository.save(newTask);
    }

    //update a Task's placement (quadrant and column)
    public Task updateTask(Integer id, Importance importance, Urgency urgency, Integer columnId, Integer userId){
        Task task = getOwnedTask(id, userId);
        task.setImportance(importance);
        task.setUrgency(urgency);
        if (columnId != null && !columnId.equals(task.getColumnId())) {
            domainColumnService.getOwnedColumn(columnId, userId);
            task.setColumnId(columnId);
        }
        return taskRepository.save(task);
    }

    //delete a Task
    public void deleteTask(Integer id, Integer userId){
        Task task = getOwnedTask(id, userId);
        taskRepository.deleteById(task.getId());
    }

    // fetch a task, refusing access to tasks in other users' columns
    private Task getOwnedTask(Integer id, Integer userId) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));
        domainColumnService.getOwnedColumn(task.getColumnId(), userId);
        return task;
    }
}
