package com.nano_d3v.lmt.services;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.nano_d3v.lmt.api.models.DomainColumn;
import com.nano_d3v.lmt.api.repositories.DomainColumnRepository;
import com.nano_d3v.lmt.api.repositories.TaskRepository;

@Service
public class DomainColumnService {

    public final DomainColumnRepository domainColumnRepository;
    public final TaskRepository taskRepository;

    public DomainColumnService(DomainColumnRepository domainColumnRepository, TaskRepository taskRepository) {
        this.domainColumnRepository = domainColumnRepository;
        this.taskRepository = taskRepository;
    }

    // get the user's columns with their tasks
    public List<DomainColumn> getAllColumns(Integer userId){
        List<DomainColumn> domainColumns = domainColumnRepository.findByUserId(userId);
        for (DomainColumn domainColumn : domainColumns) {
            domainColumn.setTasks(taskRepository.findByColumnId(domainColumn.getId()));
        }
        return domainColumns;
    }

    // addDomainColumn
    public DomainColumn addDomainColumn(String title, Integer userId) {
        DomainColumn domainColumn = new DomainColumn(title);
        domainColumn.setUserId(userId);
        return domainColumnRepository.save(domainColumn);
    }

    public void updateDomainColumn(Integer id, String title, Integer userId){
        DomainColumn domainColumn = getOwnedColumn(id, userId);
        domainColumn.setTitle(title);
        domainColumnRepository.save(domainColumn);
    }

    // deleteDomainColumn along with its tasks
    @Transactional
    public void deleteDomainColumn(Integer id, Integer userId){
        getOwnedColumn(id, userId);
        taskRepository.deleteByColumnId(id);
        domainColumnRepository.deleteById(id);
    }

    // fetch a column, refusing access to other users' columns
    public DomainColumn getOwnedColumn(Integer id, Integer userId) {
        DomainColumn domainColumn = domainColumnRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found"));
        if (!userId.equals(domainColumn.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Column not found");
        }
        return domainColumn;
    }
}
