package com.nano_d3v.lmt.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Task {

public enum Importance {
    @JsonProperty("low")
    LOW,
    @JsonProperty("high")
    HIGH
}
public enum Urgency{
    @JsonProperty("urgent")
    URGENT,
    @JsonProperty("not urgent")
    NOT_URGENT
}
    
    //A task has an id, content, importance, urgency, columnId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "importance")
    private Importance importance;

    @Column(name = "urgency")
    private Urgency urgency;

    // Foreign Key of the column
    @Column(name = "column_id") // temporary
    private Integer columnId;

    public Task() {
    }

    public Task(String content, Importance importance, Urgency urgency, Integer columnId) {
        this.content = content;
        this.importance = importance;
        this.urgency = urgency;
        this.columnId = columnId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }

    public Importance getImportance() {
        return importance;
    }

    public void setImportance(Importance importance) {
        this.importance = importance;
    }

    public Urgency getUrgency() {
        return urgency;
    }

    public void setUrgency(Urgency urgency) {
        this.urgency = urgency;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }
}
