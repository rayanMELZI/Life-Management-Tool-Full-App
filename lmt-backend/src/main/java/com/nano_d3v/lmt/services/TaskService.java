package com.nano_d3v.lmt.services;

import java.util.List;
import com.nano_d3v.lmt.api.models.Task;
import com.nano_d3v.lmt.api.models.Task.Importance;
import com.nano_d3v.lmt.api.models.Task.Urgency;

import org.springframework.stereotype.Service;

import com.nano_d3v.lmt.api.repositories.TaskRepository;

@Service
public class TaskService {

    public final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    //get a Column Tasks By ColumnId
    public List<Task> getColumnTasksByColumnId(Integer columnId){
        return taskRepository.findByColumnId(columnId);
    }

    //add a new Task
    public Task addTask(String content, Importance importance, Urgency urgency, Integer columnId){ 
        Task newTask = new Task(content, importance, urgency, columnId);
        return taskRepository.save(newTask);
    }

    //update a Task's placement (quadrant and column)
    public Task updateTask(Integer id, Importance importance, Urgency urgency, Integer columnId){
        Task task = taskRepository.findById(id).orElseThrow();
        task.setImportance(importance);
        task.setUrgency(urgency);
        if (columnId != null) {
            task.setColumnId(columnId);
        }
        return taskRepository.save(task);
    }

    //delete a Task
    public void deleteTask(Integer id){
        taskRepository.deleteById(id);
    }

}
