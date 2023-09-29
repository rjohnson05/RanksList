package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for goals. Contains all business logic relating to goals.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Service
public class GoalServiceImpl implements GoalService {


    private GoalRepository goalRepo;

    /**
     * List of all goals that have been entered in the system
     */
    private List<GoalServiceImpl> allGoals = new ArrayList<GoalServiceImpl>();

    /**
     * True if goal is completed, false otherwise
     */
    private boolean completed;


    /**
     * Constructor for the Goal Service, creating an Ad service.
     *
     * @param goalRepo Goal repository that contains the database
     */
    public GoalServiceImpl(GoalRepository goalRepo) {
        this.goalRepo = goalRepo;
    }


    @Override
    public boolean newGoal(String name, String description) {
        Goal goal = new Goal(name, description);
        goalRepo.save(goal);
        return true;
    }

    /**
     * Adds a goal to the database.
     *
     * @param goal
     */
    public void addGoal(GoalServiceImpl goal){
        allGoals.add(goal);
    }

    @Override
    public List<Goal> getAllGoals() {
        return goalRepo.findAll();
    }

    @Override
    public Goal deleteGoal(Integer id){
        Goal deletedGoal = goalRepo.getReferenceById(id);
        goalRepo.deleteById(id);
        return deletedGoal;
    }
}


