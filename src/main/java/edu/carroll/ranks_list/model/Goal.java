package edu.carroll.ranks_list.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class Goal {

    @Id
    @GeneratedValue
    private Integer id;
    private String first_name;
    private String last_name;
    private String description;

    public Goal() {
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getDescription() {
        return description;
    }



}
