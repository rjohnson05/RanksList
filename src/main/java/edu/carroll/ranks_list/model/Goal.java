package edu.carroll.ranks_list.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ad_id")
    @JsonIgnore
    private Ad ad;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    /**
     * No argument constructor for the Goal model. Creates a default Gaol object with no information.
     */
    public Goal() {
    }

    /**
     * Constructor for the Goal model. Creates a default Gaol object with no information.
     *
     * @param description String representing the text of the goal
     * @param ad          Ad object representing the advertisement the goal is connected to
     * @param user        User object representing the user creating the goal
     */
    public Goal(String description, Ad ad, User user) {
        this.description = description;
        this.ad = ad;
        this.user = user;
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
     * Sets the ID of the goal
     *
     * @param id integer representing the ID of the goal
     */
    public void setId(int id) {
        this.id = id;
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
     * Sets the description for the goal.
     *
     * @param description String object containing the description of the goal
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the parent Ad object to the goal.
     *
     * @return Ad object the goal is created for
     */
    public Ad getAd() {
        return ad;
    }

    /**
     * Sets the Ad object the goal is being created for.
     *
     * @param ad Ad object the goal is being created for
     */
    public void setAd(Ad ad) {
        this.ad = ad;
    }

    /**
     * Returns the User object creating the goal.
     *
     * @return User object creating the goal
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the User object creating the goal.
     *
     * @param user User object creating the goal
     */
    public void setUser(User user) {
        this.user = user;
    }

    public boolean equals(Goal goal) {
        return goal.getDescription() == this.getDescription();
    }

    public int hashcode() {
        return Objects.hash(description);
    }

    private static final String EOL = System.lineSeparator();

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Goal :").append(getDescription()).append(EOL);
        return builder.toString();
    }
}