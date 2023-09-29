package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;


public class GoalForm {

    @NotNull
    private String description;

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
