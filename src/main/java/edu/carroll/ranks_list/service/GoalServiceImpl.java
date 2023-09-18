package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class GoalServiceImpl implements GoalService {

    /**
     * List of all goals that have been entered in the system
     */
    private List<GoalServiceImpl> allGoals = new ArrayList<GoalServiceImpl>();

    /**
     * True if goal is completed, false otherwise
     */
    private boolean completed;


    /**
     * Constructor for each goal
     * @param ad
     */
    public GoalServiceImpl(AdServiceImpl ad){

    }

    public void addGoal(GoalServiceImpl goal){
        allGoals.add(goal);
    }

    public void setGoal(AdServiceImpl ad){
        //ad.addGoal(this.goalDescription);
        this.completed = false;
    }

    private void completedGoal(Ad ad){
        this.completed = true;
    }

}
