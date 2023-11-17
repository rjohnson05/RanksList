package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;

/**
 * Form for creating advertisements. Contains all possible user input used in the creation of an advertisement.
 *
 * @author Hank Rugg, Ryan Johnson
 */
public class AdForm {

    @NotNull
    private String name;

    @NotNull
    private Float price;

    private String description;

    public AdForm() {
    }

    /**
     * Returns the name supplied by the user.
     *
     * @return String containing the name supplied by the user for the advertisement
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name field to that supplied by the user.
     *
     * @param name String containing the name supplied by the user for the advertisement
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the price supplied by the user.
     *
     * @return Float containing the price supplied by the user for the advertisement
     */
    public Float getPrice() {
        return price;
    }

    /**
     * Sets the price field to that supplied by the user.
     *
     * @param price Float containing the price supplied by the user for the advertisement
     */
    public void setPrice(Float price) {
        this.price = price;
    }

    /**
     * Returns the description supplied by the user.
     *
     * @return String containing the description supplied by the user for the advertisement
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description field to that supplied by the user.
     *
     * @param description String containing the description supplied by the user for the advertisement
     */
    public void setDescription(String description) {
        this.description = description;
    }
}