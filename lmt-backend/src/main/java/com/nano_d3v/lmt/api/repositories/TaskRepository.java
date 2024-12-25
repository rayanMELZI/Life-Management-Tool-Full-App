package com.nano_d3v.lmt.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nano_d3v.lmt.api.models.Task;

@Repository
public interface TaskRepository extends CrudRepository<Task, Integer> {
    
    @SuppressWarnings("null")
    List<Task> findAll();

    List<Task> findByColumnId(Integer columnId);
}
