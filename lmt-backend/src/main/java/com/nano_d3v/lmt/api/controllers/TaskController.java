package com.nano_d3v.lmt.api.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nano_d3v.lmt.api.models.Task;
import com.nano_d3v.lmt.services.TaskService;

@RestController
@RequestMapping("/api/task")
public class TaskController {

    public final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/add")
    public Task addTask(@RequestBody Task task){
        return taskService.addTask(task.getContent(), task.getImportance(), task.getUrgency(), task.getColumnId());
    }

    //update a task's quadrant/column after a drag
    @PutMapping("/update")
    public Task updateTask(@RequestBody Task task){
        return taskService.updateTask(task.getId(), task.getImportance(), task.getUrgency(), task.getColumnId());
    }

    @DeleteMapping("/delete")
    public String deleteTask(@RequestBody Task task){
        taskService.deleteTask(task.getId());
        return "Task deleted successfully";
    }
}
