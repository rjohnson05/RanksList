package edu.carroll.RanksList.service;

import java.util.ArrayList;



public class GoalServiceImpl implements GoalService {

    /**
     * List of all goals that have been entered in the system
     */
    private ArrayList<GoalServiceImpl> allGoals = new ArrayList<GoalServiceImpl>();

    /**
     * Unique goal id corresponding to amount of total goals in system
     */
    private final int goalID;

    /**
     * Description of goal
     */
    private final String goalDesc;


    /**
     * Constructor for each goal
     * @param goalDesc
     */
    public GoalServiceImpl(String goalDesc){
        this.goalDesc = goalDesc;
        this.goalID = allGoals.size() + 1;

    }

    private void addGoal(GoalServiceImpl goal){
        allGoals.add(goal);
    }


}
