package edu.carroll.ranks_list.form;

import jakarta.validation.constraints.NotNull;

/**
 * Form for creating goals. Contains all possible user input used in the creation of a goal.
 *
 * @author Hank Rugg, Ryan Johnson
 */
public class GoalForm {

    @NotNull
    private String description;

    @NotNull
    private Integer ad_id;

    /**
     * Returns the description supplied by the user.
     *
     * @return String containing the description supplied by the user
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description to that supplied by the user.
     *
     * @param description String containing the description supplied by the user for the goal
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the ID of the advertisement connected to the goal.
     *
     * @return Integer containing the ID of the advertisement connected to the goal
     */
    public Integer getAdId() {
        return ad_id;
    }

    /**
     * Sets the ID of the connected advertisement.
     *
     * @param ad_id Integer containing the ID of advertisement connected to the goal
     */
    public void setAdId(Integer ad_id) {
        this.ad_id = ad_id;
    }

}
