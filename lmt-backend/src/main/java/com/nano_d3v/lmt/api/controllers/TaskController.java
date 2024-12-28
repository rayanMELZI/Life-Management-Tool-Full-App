package com.nano_d3v.lmt.api.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nano_d3v.lmt.api.models.Task;
import com.nano_d3v.lmt.services.TaskService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/task")
public class TaskController {

    public final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    // @PostMapping("/add")
    // public String addTask(@RequestBody Task task){
    //     TaskService.addTask(task.getContent(), task.getImportance(), task.getUrgency(), task.getColumnId());
    //     // TaskService.addTask(task);
    //     return "Task added successfully";
    // }
}
