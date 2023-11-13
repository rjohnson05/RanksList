package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.GoalForm;
import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.service.AdService;
import edu.carroll.ranks_list.service.GoalService;
import edu.carroll.ranks_list.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API for HTTP requests regarding goals. Specifies how to handle HTTP requests that involve goal data.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@RestController
@CrossOrigin(value="http://localhost:3000", allowCredentials = "true")
public class GoalController {

    private final GoalService goalService;
    private final AdService adService;
    private final UserService userService;

    /**
     * Constructor for the Goal Controller. Creates a service for the goals business logic.
     *
     * @param goalService contains all business logic related to goal data
     * @param adService
     */
    public GoalController(GoalService goalService, AdService adService, UserService userService) {
        this.goalService = goalService;
        this.adService = adService;
        this.userService = userService;
    }

    /**
     * Creates a new Goal and adds it to the database.
     *
     * @param goalForm Goal data to be added to the database
     * @return Goal object that has been successfully added to the database
     */
    @PostMapping("/goals")
    boolean newGoal(@RequestBody GoalForm goalForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int currentUserId = Integer.parseInt((String) session.getAttribute("userID"));

        User attachedUser = userService.getReferenceById(currentUserId);
        Ad attatchedAd = adService.getReferenceById(goalForm.getAdId());
        return goalService.newGoal(goalForm.getDescription(), attatchedAd, attachedUser);
    }

    /**
     * Gets all the current goals.
     *
     * @return List of all Goal objects in the database
     */
    @GetMapping("/goals")
    List<Goal> getAllGoals() {
        return goalService.getAllGoals();
    }

    /**
     * Gets all the current goals.
     *
     * @return List of all Goal objects in the database
     */
    @GetMapping("/individual_goals/{adId}")
    List<Goal> getIndividualGoals(@PathVariable("adId") Integer adId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        int userId = Integer.parseInt((String) session.getAttribute("userID"));

        return goalService.getIndividualGoals(adId, userId);
    }

    /**
     * Deletes a selected goal and removes it from the database.
     *
     * @param id Integer representing the ID of the selected goal
     */
    @DeleteMapping("/individual_goals/{id}")
    public void deleteGoal(@PathVariable("id") Integer id) {
        goalService.deleteGoal(id);
    }
}