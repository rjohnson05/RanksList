package edu.carroll.ranks_list.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

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

    @Column(name = "price")
    private Float price;

    @Column(name = "description")
    private String description;

    @Column(name = "starred")
    private Boolean starred = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

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
     * Returns the status of the advertisement.
     *
     * @return True if the advertisement is starred; False otherwise
     */
    public Boolean getStarred() {
        return starred;
    }

    /**
     * Sets the status of the advertisement.
     *
     * @param starred True if the advertisement is being starred; False otherwise
     */
    public void setStarred(Boolean starred) {
        this.starred = starred;
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
                TAB + "Starred Status: " + starred + EOL +
                TAB + "Created by " + user + EOL +
                "]" + EOL;
    }
}
