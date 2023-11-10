package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;

public class AdForm {
    @NotNull
    private String name;

    @NotNull
    private String description;

    private Float price;

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}