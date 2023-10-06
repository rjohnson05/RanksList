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

    @Column(name = "price", nullable = false)
    private Float price;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "starred")
    private Boolean starred = Boolean.FALSE;

    @Column(name = "image")
    private byte[] image;

    /**
     * Constructor for the Ad model. Creates a default Ad object with no information.
     */
    public Ad() {
    }

    public Ad(String name, Float price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
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
     * Returns the image associated with the advertisement.
     *
     * @return list of bytes containing the image data
     */
    public byte[] getImage() {
        return image;
    }

    /**
     * Sets the image associated with the advertisement.
     *
     * @param image list of bytes containing the image data
     */
    public void setImage(byte[] image) {
        this.image = image;
    }

    private static final String EOL = System.lineSeparator();
    private static final String TAB = "\t";

    public String toString() {
        StringBuilder builder = new StringBuilder();
//        builder.append("Ad #").append(getId()).append(" [").append(EOL);
        builder.append(TAB).append("Name: ").append(name).append(EOL);
        builder.append(TAB).append("Price: ").append(price).append(EOL);
        builder.append(TAB).append("Description: ").append(description).append(EOL);
        builder.append(TAB).append("Starred Status: ").append(starred).append(EOL);
        builder.append("]").append(EOL);
        return builder.toString();
    }
}

