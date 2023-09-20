package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.repository.GoalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class GoalController {

    private static final Logger log = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private GoalRepository goalRepository;

    @PostMapping("/goals")
    Goal newGoal(@RequestBody Goal newGoal) {
        System.out.println("Posted goal");
        return goalRepository.save(newGoal);
    }

    @GetMapping("/goals")
    List<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    @DeleteMapping("/goals/{id}")
    public void deleteGoal(@PathVariable("id") Integer id) {
        System.out.println("Delete Mapping");
        // Ad deletedAd = adRepository.getReferenceById(id);
        goalRepository.deleteById(id);
        System.out.println(id);
        log.info("Goal #{id} deleted");


    }
}
