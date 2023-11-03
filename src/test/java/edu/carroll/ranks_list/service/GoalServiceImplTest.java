package edu.carroll.ranks_list.service;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertFalse;
import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@Transactional
public class GoalServiceImplTest {

    private static final String name = "Test";

    private static final String description = "Testing the goals";

    private static final Integer adId = 0;

    @Autowired
    private GoalService goalService;

    @BeforeEach
    public void beforeTest() {
        goalService.deleteAllGoals();
    }

    @Test
    public void validateNewGoalSuccess(){
        assertTrue("validateNewGoalSuccess: Should pass with a new goal created returning true", goalService.newGoal(name, description, adId));
    }
    @Test
    public void validateNewGoalNullName(){
        assertFalse("validateNewGoalFail: Should fail with a new goal not created", goalService.newGoal(null, description, adId));
    }

    @Test
    public void validateNewGoalNullDesc(){
        assertFalse("validateNewGoalNullDesc: Should fail with a new goal not created", goalService.newGoal(name, null, adId));
    }

    @Test
    public void validateNewGoalNullId(){
        assertFalse("validateNewGoalNullId: Should fail with a new goal not created", goalService.newGoal(name, description, null));
    }

    @Test
    public void validateAllGoalsSize(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSize: Should pass with the size of all goals being 1", goalService.getAllGoals().size() == 1);
    }

    @Test
    public void validateAllGoalsSizeFail(){
        goalService.newGoal(name, description, adId);
        assertFalse("validateAllGoalsSize: Should fail with the size of all goals being 1, not 2", goalService.getAllGoals().size() == 2);
    }


    @Test
    public void validateAllGoalsSpecificGoalDesc(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSpecificGoalDesc: Should pass with the description of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getDescription() == description);
    }

    @Test
    public void validateAllGoalsSpecificGoalDescFail(){
        goalService.newGoal(name, description, adId);
        assertFalse("validateAllGoalsSpecificGoalDesc: Should fail with the description of the goal not being equal to the name", goalService.getAllGoals().get(0).getDescription() == name);
    }

    @Test
    public void validateAllGoalsSpecificGoalAdId(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateAllGoalsSpecificGoalAdId: Should pass with the ad id of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getAd_id() == adId);
    }

    @Test
    public void validateAllGoalsSpecificGoalAdIdFail(){
        goalService.newGoal(name, description, adId);
        assertFalse("validateAllGoalsSpecificGoalAdId: Should fail with the ad id of the goal not being equal to the 100", goalService.getAllGoals().get(0).getAd_id() == 100);
    }

    @Test
    public void validateGetIndividualGoal(){
        goalService.newGoal(name, description, adId);
        assertTrue("validateGetIndividualGoals: Should pass with the individual goal being equal to the goal that was set", goalService.getIndividualGoals(adId).get(0) == goalService.getAllGoals().get(0));
    }

    @Test
    public void validateGetIndividualGoalFail(){
        goalService.newGoal(name, description, adId);
        assertFalse("validateGetIndividualGoalFail: Should fail with the individual goal not being equal to null", goalService.getIndividualGoals(adId).get(0) == null);
    }

    @Test
    public void validateNewGoalSizeDuplicate(){
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
