package edu.carroll.ranks_list.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

/**
 * Model for stars. Contains all information regarding whether an ad is starred for a specific user.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@Entity
@Table(name = "star")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Star {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "starred")
    private Boolean starred = Boolean.TRUE;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    @JsonIgnore
    private Ad ad;

    /**
     * Constructor for the Star model. Creates a default Star object that is not starred.
     */
    public Star() {
    }

    /**
     * Constructor for the Star model. Creates a Star object assigned to the designated advertisement and user.
     */
    public Star(Ad ad, User user) {
        this.ad = ad;
        this.user = user;
    }

    /**
     * Returns whether the star is starred or not.
     *
     * @return true if the star is starred; false otherwise
     */
    public Boolean getStarred() {
        return starred;
    }

    /**
     * Sets the starred status.
     *
     * @param starred true if the star is starred; false otherwise
     */
    public void setStarred(Boolean starred) {
        this.starred = starred;
    }

    /**
     * Returns the user changing the star for an ad.
     *
     * @return User object changing the star for an ad
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user that is changing the star for an ad.
     *
     * @param user User object changing the star for an ad
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns the ad the star is assigned to.
     *
     * @return Ad object the star is assigned to
     */
    public Ad getAd() {
        return ad;
    }

    /**
     * Sets the ad the star is assigned to.
     *
     * @param ad Ad object the star is assigned to
     */
    public void setAd(Ad ad) {
        this.ad = ad;
    }

    public int hashCode() {
        return super.hashCode();
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    public String toString() {
        return "Star #" + id + " [" + EOL +
                TAB + "Status: " + starred + EOL +
                TAB + "Describing Ad " + ad + EOL +
                "]" + EOL;
    }
}
