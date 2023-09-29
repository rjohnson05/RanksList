package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.form.GoalForm;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.repository.GoalRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class GoalServiceImpl implements GoalService {

    private GoalRepository goalRepo;

    /**
     * Constructor for each goal
     */
    public GoalServiceImpl(GoalRepository goalRepo) {
        this.goalRepo = goalRepo;
    }

    @Override
    public boolean newGoal(String name, String description){
        Goal goal = new Goal(name, description);
        goalRepo.save(goal);
        return true;
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


