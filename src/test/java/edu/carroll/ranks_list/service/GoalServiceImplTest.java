package edu.carroll.ranks_list.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class GoalServiceImplTest {

    private static final String name = "TestGoal";

    private static final String description = "Testing the goals";

    private static final Integer adId = 0;

    @Autowired
    private GoalService goalService;

//    private Goal fakeGoal = new Goal(name, description, ad_id);

    @BeforeEach
    public void beforeTest() {
        goalService.deleteAllGoals();
    }

    @Test
    public void validateNewGoalSuccess(){
        assertTrue("validateAllGoalsSize: Should pass with a new goal created returning true", goalService.newGoal(name, description, adId));
    }

    @Test
    public void validateAllGoalsSize(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSize: Should pass with the size of all goals being 1", goalService.getAllGoals().size() == 1);
    }

    @Test
    public void validateAllGoalsSpecificGoalName(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSpecificGoalName: Should pass with the name of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getName() == name);
    }

    @Test
    public void validateAllGoalsSpecificGoalDesc(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSpecificGoalDesc: Should pass with the description of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getDescription() == description);
    }

    @Test
    public void validateAllGoalsSpecificGoalAdId(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSpecificGoalAdId: Should pass with the ad id of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getAd_id() == adId);
    }

    @Test
    public void validateNewGoalDuplicate(){
        goalService.newGoal(name, description, adId);
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSize: Should pass size of goals being 2", goalService.getAllGoals().size() == 2);
    }

    @Test
    public void validateDeleteAllGoals(){
        goalService.newGoal(name, description, adId);
        goalService.deleteAllGoals();
        assertTrue("validateDeleteAllGoals: Should pass with size being 0", goalService.getAllGoals().size() == 0);

    }

    @Test
    public void validateDeleteAllGoalsMultiple(){
        goalService.newGoal(name, description, adId);
        goalService.newGoal(name, description, adId);
        goalService.deleteAllGoals();
        assertTrue("validateDeleteAllGoalsMultiple: Should pass with size being 0", goalService.getAllGoals().size() == 0);

    }



}
