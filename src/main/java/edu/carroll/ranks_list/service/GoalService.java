package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Goal;

import java.util.List;

/**
 * Interface for goal services. Contains all business methods relating to goals.
 *
 * @author Hank Rugg, Ryan Johnson
 */
public interface GoalService {

    /**
     * Creates a new goal and adds it to the repo
     *
     * @param description String representing the main text of the goal
     * @param ad          advertisement the goal is being saved to
     * @param user        User object creating the goal
     * @return true if the goal is successfully created; false otherwise
     */
    boolean newGoal(String description, Ad ad, int user_id);

    /**
     * Returns all gaols present in the database.
     *
     * @return List of all goals present in the database
     */
    List<Goal> getAllGoals();

    /**
     * Finds goals that are related to the specific ad.
     *
     * @param adId   int representing the ID of the advertisement the goals are being pulled for
     * @param userId int representing the ID of the user the goals are being pulled for
     * @return List of goals belonging to the advertisement with the designated ID
     */
    List<Goal> getIndividualGoals(int adId, int userId);

    /**
     * Deletes the specified goal from the database.
     *
     * @param id int representing the ID of the goal to be deleted
     * @return Goal that was deleted
     */
    boolean deleteGoal(int id);

    /**
     * Deletes all goals from the database.
     *
     * @return true if all goals are successfully deleted from the database; false otherwise
     */
    boolean deleteAllGoals();

}
