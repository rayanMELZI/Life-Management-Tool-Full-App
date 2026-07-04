package com.nano_d3v.lmt.services;


import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // getAllColumns with their tasks
    public List<DomainColumn> getAllColumns(){
        List<DomainColumn> domainColumns = domainColumnRepository.findAll();
        for (DomainColumn domainColumn : domainColumns) {
            domainColumn.setTasks(taskRepository.findByColumnId(domainColumn.getId()));
        }
        return domainColumns;
    }

    // addDomainColumn
    public DomainColumn addDomainColumn(String title) {
        DomainColumn domainColumn = new DomainColumn(title);
        return domainColumnRepository.save(domainColumn);
    }

    public void updateDomainColumn(Integer id, String title){
        DomainColumn domainColumn = domainColumnRepository.findById(id).get();
        domainColumn.setTitle(title);
        domainColumnRepository.save(domainColumn);
    }

    // deleteDomainColumn along with its tasks
    @Transactional
    public void deleteDomainColumn(Integer id){
        taskRepository.deleteByColumnId(id);
        domainColumnRepository.deleteById(id);
    }
}
