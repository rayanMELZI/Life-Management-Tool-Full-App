package com.nano_d3v.lmt.api.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nano_d3v.lmt.api.models.DomainColumn;
import com.nano_d3v.lmt.services.DomainColumnService;

@RestController
// @CrossOrigin(origins = "http://localhost:5173")
// @CrossOrigin(origins = "http://localhost:8080")
@CrossOrigin(origins = "https://life-management-tool.onrender.com")
@RequestMapping("/api/domainColumn")
public class DomainColumnController {

    public final DomainColumnService domainColumnService;

    public DomainColumnController(DomainColumnService domainColumnService) {
        this.domainColumnService = domainColumnService;
    }

    @PostMapping("/add")
    public String addColumn(
        @RequestBody DomainColumn domainColumn
    ) {
        domainColumnService.addDomainColumn(domainColumn.getTitle());
        return "Column added successfully";
    }
    
    // @GetMapping("/id")
    // public DomainColumn[] getColumnById(@RequestParam Integer id) {
    //     //return all columns
    // }

    //return all columns
    @GetMapping("/all")
    public List<DomainColumn> getAllColumns() {
        List<DomainColumn> domainColumns = domainColumnService.getAllColumns();
        return domainColumns;
    }
    
    //update column's title
    @PutMapping("/update")
    public String updateColumn(
        @RequestBody DomainColumn domainColumn
    ) {
        domainColumnService.updateDomainColumn( domainColumn.getId(), domainColumn.getTitle());
        return "Column updated successfully";
    }
    
    @DeleteMapping("/delete")
    public String deleteColumn(@RequestBody DomainColumn domainColumn) {
        //remove column
        domainColumnService.deleteDomainColumn(domainColumn.getId());
        return "Column deleted successfully";
    }
}