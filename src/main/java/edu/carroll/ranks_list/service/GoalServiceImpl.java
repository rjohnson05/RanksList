package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.repository.GoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for goals. Contains all business logic relating to goals.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Service
public class GoalServiceImpl implements GoalService {

    private static final Logger log = LoggerFactory.getLogger(GoalServiceImpl.class);
    private final GoalRepository goalRepo;

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
     *
     * @param description String representing the main text of the goal
     * @param ad advertisement the goal is being saved to
     *
     * @return true if the goal is successfully created; false otherwise
     */
    @Override
    public boolean newGoal(String description, Ad ad) {
        // checking to see if the ad should be created
        if (description == null){
            log.debug("Description parameter was null when creating a new goal");
            return false;
        }
        if (ad == null) {
            log.debug("ad parameter was null when creating a new goal");
            return false;
        }
        // creating the add
        Goal goal = new Goal(description, ad);
        goalRepo.save(goal);
        log.info("New goal created for Ad with id:{}", ad.getId());
        return true;
    }

    /**
     * Returns all gaols present in the database.
     *
     * @return List of all goals present in the database
     */
    @Override
    public List<Goal> getAllGoals() {
        log.debug("Accessing all goals");
        return goalRepo.findAll();
    }

    /**
     * Finds goals that are related to the specific ad.
     *
     * @param adId Integer representing the ID of the advertisement the goals is being saved to
     *
     * @return List of goals belonging to the advertisement with the designated ID
     */
    @Override
    public List<Goal> getIndividualGoals(Integer adId) {
        log.debug("Accessing goal :{}", adId);
        return goalRepo.findByadId(adId);
    }

    /**
     * Deletes the specified goal from the database.
     *
     * @param id Integer representing the ID of the goal to be deleted
     *
     * @return Goal that was deleted
     */
    @Override
    public boolean deleteGoal(Integer id){
        goalRepo.deleteById(id);
        log.info("Deleted goal {}", id);
        return true;
    }

    /**
     * Deletes all goals from the database.
     *
     * @return true if all goals are successfully deleted from the database; false otherwise
     */
    @Override
    public boolean deleteAllGoals(){
        // get all goals and delete them singularly
        for(Goal g : getAllGoals()){
            deleteGoal(g.getId());
        }
        log.info("Deleted all goals.");
        return true;
    }

}
