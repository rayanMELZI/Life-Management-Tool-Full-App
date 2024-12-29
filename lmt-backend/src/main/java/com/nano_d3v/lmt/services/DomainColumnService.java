package com.nano_d3v.lmt.services;


import java.util.List;

import org.springframework.stereotype.Service;

import com.nano_d3v.lmt.api.models.DomainColumn;
import com.nano_d3v.lmt.api.repositories.DomainColumnRepository;

@Service
public class DomainColumnService {

    public final DomainColumnRepository domainColumnRepository;

    public DomainColumnService(DomainColumnRepository domainColumnRepository) {
        this.domainColumnRepository = domainColumnRepository;
    }
    
    // getAllColumns
    public List<DomainColumn> getAllColumns(){
        return domainColumnRepository.findAll();
    }

    // addDomainColumn
    public void addDomainColumn(String title) {
        DomainColumn domainColumn = new DomainColumn(title);
        domainColumnRepository.save(domainColumn);
    }

    public void updateDomainColumn(Integer id, String title){
        DomainColumn domainColumn = domainColumnRepository.findById(id).get();
        domainColumn.setTitle(title);
        domainColumnRepository.save(domainColumn);
    }

    // deleteDomainColumn
    public void deleteDomainColumn(Integer id){
        domainColumnRepository.deleteById(id);
    }
}
