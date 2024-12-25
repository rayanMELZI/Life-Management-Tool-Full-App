package com.nano_d3v.lmt.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nano_d3v.lmt.api.models.DomainColumn;

@Repository
public interface DomainColumnRepository extends CrudRepository<DomainColumn, Integer> {
    
    @SuppressWarnings("null")
    List<DomainColumn> findAll();
}
