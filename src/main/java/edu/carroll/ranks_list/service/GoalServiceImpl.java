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
    public boolean newGoal(String name, String description, Integer ad_id) {
        Goal goal = new Goal(name, description, ad_id);
        goalRepo.save(goal);
        return true;
    }

    @Override
    public List<Goal> getAllGoals() {
        return goalRepo.findAll();
    }

    @Override
    public List<Goal> getIndividualGoals(Integer adId) {
        List<Goal> all = goalRepo.findAll();
        List<Goal> individual = new ArrayList<>();
        for (Goal g : all){
            if (g.getAd_id().equals(adId)){
                individual.add(g);
            }
        }
        return individual;
    }

    @Override
    public Goal deleteGoal(Integer id){
        Goal deletedGoal = goalRepo.getReferenceById(id);
        goalRepo.deleteById(id);
        return deletedGoal;
    }
}


