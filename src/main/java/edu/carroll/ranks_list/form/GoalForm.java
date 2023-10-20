package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;

public class GoalForm {

    @NotNull
    private String description;

    @NotNull
    private Integer ad_id;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAdId() {
        return ad_id;
    }

    public void setAdId(Integer ad_id) {
        this.ad_id = ad_id;
    }
}
