package edu.carroll.ranks_list.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "goal")
public class Goal {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "description", nullable = false)
    private String description;

    public Goal() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(){
        return true;
    }
    @Override
    public int hashcode(){
        return Objects.hash(description);
    }


}
