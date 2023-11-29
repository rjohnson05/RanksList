package edu.carroll.ranks_list.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.lang.NonNull;

import java.util.List;

/**
 * Model for advertisements. Contains all information regarding any given ad.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@Entity
@Table(name = "ad")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Ad {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @OneToMany(mappedBy = "ad")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Goal> goals;

    @OneToMany(mappedBy = "ad")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Star> stars;

    /**
     * Constructor for the Ad model. Creates a default Ad object with no information.
     */
    public Ad() {
    }

    /**
     * Constructor for Ad model. Creates an Ad object with the prescribed name, price, description, and user.
     *
     * @param name        String representing the name of the advertisement
     * @param price       Float representing the price of the advertisement
     * @param description String representing the description of the advertisement
     * @param user        User object representing the user that created the advertisement
     */
    public Ad(String name, Float price, String description, User user) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.user = user;
    }

    /**
     * Returns the ID of the advertisement.
     *
     * @return int representing the ID of the advertisement
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the advertisement.
     *
     * @param id int representing the ID of the advertisement
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Returns the title of the advertisement.
     *
     * @return String object containing the title of the advertisement
     */
    @NonNull
    public String getName() {
        return name;
    }

    /**
     * Sets the title of the advertisement.
     *
     * @param name String object containing the title of the advertisement
     */
    public void setName(@NonNull String name) {
        this.name = name;
    }

    /**
     * Returns the price of the advertisement.
     *
     * @return Float object representing the price of the advertisement
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Sets the price of the advertisement.
     *
     * @param price Float object representing the price of the advertisement
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * Returns the description of the advertisement.
     *
     * @return String object containing the description of the advertisement
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the advertisement.
     *
     * @param description String object containing the description of the advertisement.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the user that created the advertisement.
     *
     * @return User object specifying the creator of the advertisement
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user that created the advertisement.
     *
     * @param user User object specifying the creator of the advertisement
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the goals that were created by a user for a specific ad.
     *
     * @return List of goals created by a specific user for the ad
     */
    public List<Goal> getGoals() {
        return goals;
    }

    /**
     * Sets the list of goals for the advertisement.
     *
     * @param goals List of Goal objects for the advertisement
     */
    public void setGoals(List<Goal> goals) {
        this.goals = goals;
    }

    /**
     * Returns all the stars for the advertimsement, acrosss all users.
     *
     * @return List of Star objects containing the starred status for the advertisement for a particular user
     */
    public List<Star> getStars() {
        return stars;
    }

    /**
     * Sets the list of stars for the advertisement.
     *
     * @param stars List of star objects containing the starred status for the advertisement for a particular state
     */
    public void setStars(List<Star> stars) {
        this.stars = stars;
    }

    public int hashCode() {
        return super.hashCode();
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    public String toString() {
        return "Ad #" + id + " [" + EOL +
                TAB + "Name: " + name + EOL +
                TAB + "Price: " + price + EOL +
                TAB + "Description: " + description + EOL +
                TAB + "Created by " + user + EOL +
                "]" + EOL;
    }
}
