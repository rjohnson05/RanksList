package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.GoalForm;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.service.GoalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class GoalController {

    private static final Logger log = LoggerFactory.getLogger(AdController.class);

    private GoalService goalService;

    public GoalController(GoalService goalService) {
        this.goalService = goalService;
    }


    @PostMapping("/goals")
    boolean newGoal(@RequestBody GoalForm goalForm) {
        System.out.println("Posted goal");
        return goalService.newGoal(goalForm.getName(), goalForm.getDescription());
    }

    @GetMapping("/goals")
    List<Goal> getAllGoals() {
        return goalService.getAllGoals();
    }

    @DeleteMapping("/goals/{id}")
    public void deleteGoal(@PathVariable("id") Integer id) {
        // Ad deletedAd = adRepository.getReferenceById(id);
        goalService.deleteGoal(id);
        log.info("Goal #{id} deleted");


    }
}
