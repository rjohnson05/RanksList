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

    @Column(name = "name", nullable = false)
    private String name;

    public Goal() {
    }

    public Goal(String name, String description){
        this.description = description;
        this.name = name;
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

    public boolean equals(Goal goal){
        return goal.getDescription() == this.getDescription();
    }

    public int hashcode(){
        return Objects.hash(description);
    }

    private static final String EOL = System.lineSeparator();
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        builder.append("Goal :").append(getDescription()).append(EOL);
        return builder.toString();
    }
}
