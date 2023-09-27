package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.repository.GoalRepository;
import edu.carroll.ranks_list.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API for HTTP requests regarding goals. Specifies how to handle HTTP requests that involve goal data.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@RestController
@CrossOrigin("http://localhost:3000")
public class GoalController {
    private static final Logger log = LoggerFactory.getLogger(AdController.class);

    private GoalRepository goalRepository;
    private GoalService goalService;

    /**
     * Constructor for the Goal Controller. Creates a service for the goals business logic.
     *
     * @param goalService contains all business logic related to goal data
     */
    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }

    /**
     * Creates a new Goal and adds it to the database.
     *
     * @param newGoal Goal object to be added to the database
     * @return Goal object that has been successfully added to the database
     */
    @PostMapping("/goals")
    Goal newGoal(@RequestBody Goal newGoal) {
        return goalRepository.save(newGoal);
    }

    /**
     * Gets all the current goals.
     *
     * @return List of all Goal objects in the database
     */
    @GetMapping("/goals")
    List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    /**
     * Deletes a selected goal and removes it from the database.
     *
     * @param id Integer representing the ID of the selected goal
     */
    @DeleteMapping("/goals/{id}")
    public void deleteGoal(@PathVariable("id") Integer id) {
        goalRepository.deleteById(id);
        log.info("Goal #{id} deleted");
    }
}
