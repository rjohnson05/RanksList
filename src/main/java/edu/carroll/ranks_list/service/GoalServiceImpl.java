package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.controller.AdController;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.repository.GoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(GoalServiceImpl.class);

    private GoalRepository goalRepo;


    /**
     * Constructor for the Goal Service, creating an Ad service.
     *
     * @param goalRepo Goal repository that contains the database
     */
    public GoalServiceImpl(GoalRepository goalRepo) {
        this.goalRepo = goalRepo;
    }



    /**
     * Creates a new goal and adds it to the repo
     * @param description
     * @param ad_id
     * @return
     */
    @Override
    public boolean newGoal(String description, Integer ad_id) {
            Goal goal = new Goal(description, ad_id);
            goalRepo.save(goal);
            log.info("New goal created for Ad with id:{}", ad_id);
            return true;
    }

    /**
     *
     * @return All goals that have been saved
     */
    @Override
    public List<Goal> getAllGoals() {
        return goalRepo.findAll();
    }

    /**
     * Finds goals that are related to the specific ad
     * @param adId
     * @return
     */
    @Override
    public List<Goal> getIndividualGoals(Integer adId) {
        return goalRepo.findByadId(adId);
    }

    /**
     * Deletes the specified goal from the database
     * @param id
     * @return Goal that was deleted
     */
    @Override
    public Goal deleteGoal(Integer id){
        Goal deletedGoal = goalRepo.getReferenceById(id);
        goalRepo.deleteById(id);
        log.info("Deleted goal {}", id);
        return deletedGoal;
    }

    /**
     * Deletes all goals from the database
     * @return true
     */
    @Override
    public boolean deleteAllGoals(){
        for(Goal g : getAllGoals()){
            deleteGoal(g.getId());
        }
        log.info("Deleted all goals.");
        return true;
    }

}


