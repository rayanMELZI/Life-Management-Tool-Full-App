package com.nano_d3v.lmt.api.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class DomainColumn {
    //A column has an id, title, and a list of tasks

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    // Tasks live in their own table (keyed by column_id) and are attached here
    // when columns are sent to the client
    @Transient
    private List<Task> tasks = new ArrayList<>();

    public DomainColumn() {
    }

    public DomainColumn(String title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    
    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    // @Override
    // public String toString() {
    //     return "Column{" +
    //             "name='" + name + '\'' +
    //             '}';
    // }
}