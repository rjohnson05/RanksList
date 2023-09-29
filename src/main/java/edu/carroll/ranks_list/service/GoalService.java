package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Goal;

import java.util.List;

public interface GoalService {

    public boolean newGoal(String name, String description);

    public List<Goal> getAllGoals();

    public Goal deleteGoal(Integer id);


}
