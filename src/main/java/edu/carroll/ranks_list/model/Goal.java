package edu.carroll.ranks_list.model;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Model for goals. Contains all information regarding any given goal.
 *
 * @author Hank Rugg, Ryan Johnson
 */
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

    @Column(name = "adId", nullable = false)
    private Integer adId;

    /**
     *
     * @return ad_Id for this goal
     */
    public Integer getAd_id() {
        return adId;
    }

    /**
     * Sets ad id for this goal
     * @param adId
     */
    public void setAd_id(Integer adId) {
        this.adId = adId;
    }

    /**
     * No argument constructor for the Goal model. Creates a default Gaol object with no information.
     */
    public Goal() {
    }

    /**
     * Constructor for the Goal model. Creates a default Gaol object with no information.
     */
    public Goal(String name, String description, Integer adId){
        this.description = description;
        this.name = name;
        this.adId = adId;
    }


    /**
     * Returns the ID of the goal.
     *
     * @return Integer object representing the ID of the goal
     */

    public Integer getId() {
        return id;
    }

    /**
     * Returns the description of the goal.
     *
     * @return String object containing the description of the goal
     */
    public String getDescription() {
        return description;
    }

    /**
     * Getter for the name of the goal
     *
     * @return name of goal
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of goal
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }


    /**
     * Sets the description for the goal.
     *
     * @param description String object containing the description of the goal
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Equals method, compares the descriptions of the goals
     * @param goal
     * @return
     */
    public boolean equals(Goal goal) {
        return goal.getDescription() == this.getDescription();
    }


    /**
     * Java hashcode
     * @return hashcode of the description
     */
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
