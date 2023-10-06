package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Goal;

import java.util.List;

public interface GoalService {

    public boolean newGoal(String name, String description, Integer ad_id);

    public List<Goal> getAllGoals();

    public List<Goal> getIndividualGoals(Integer ad_id);

    public Goal deleteGoal(Integer id);

    public boolean deleteAllGoals();

}
