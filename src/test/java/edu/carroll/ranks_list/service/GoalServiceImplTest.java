package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class GoalServiceImplTest {


    private static final String description = "Testing the goals";

    private final User user = new User("username", "password");

    private final Ad ad = new Ad("AdName", 10.00F, "This is the description", user);

    @Autowired
    private GoalService goalService;

    // testing new goal method
    @Test
    public void validateNewGoalSuccess(){
        int size = goalService.getAllGoals().size();
        assertTrue("validateNewGoalSuccess: Should pass with a new goal created returning true", goalService.newGoal(description, ad));
        List<Goal> individualGoals = goalService.getIndividualGoals(ad.getId());
        assertEquals("validateNewGoalSuccess: Newly added goal did not increase number of goals for ad", individualGoals.size(), size+1);
        if (individualGoals.size() == 1){
            assertTrue("validateNewGoalSuccess: Set description of goal does not match fetched goal", individualGoals.get(0).getDescription().equals(description));
        }
    }


    // testing new goal method
    @Test
    public void validateNewGoalNullDesc(){
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalNullDesc: Should fail with a new goal not created", goalService.newGoal(null, ad));
        assertTrue("validateNewGoalNullDesc: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

    // testing new goal method
    @Test
    public void validateNewGoalNullId(){
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalNullId: Should fail with a new goal not created", goalService.newGoal(description, null));
        assertTrue("validateNewGoalNullId: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSize(){
        goalService.newGoal(description, ad);
        assertTrue("validateAllGoalsSize: Should pass with the size of all goals being 1", goalService.getAllGoals().size() == 1);
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSizeMultiple(){
        for (int i = 1; i < 11; i++){
            goalService.newGoal(description, ad);
            assertTrue("validateAllGoalsSizeMultiple: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
        }
        assertTrue("validateAllGoalsSizeMultiple: The size of the list of goals should be 10", goalService.getAllGoals().size() == 10);

    }

    // testing get all goals
    @Test
    public void validateAllGoalsSizeMultipleWithDeletion(){
        for (int i = 1; i < 11; i++){
            goalService.newGoal(description, ad);
            assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
        }
        int size = goalService.getAllGoals().size();
        goalService.deleteGoal(goalService.getAllGoals().get(0).getId());
        assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals should be 9", goalService.getAllGoals().size() == size-1);
    }

    // testing delete goal
    @Test
    public void deleteSpecificGoal(){
        goalService.newGoal(description, ad);
        assertTrue("deleteSpecificGoal: The size of the list of goals should be 1", goalService.getAllGoals().size() == 1);
        goalService.deleteGoal(goalService.getAllGoals().get(0).getId());
        assertTrue("deleteSpecificGoal: The size of the list of goals should be 9", goalService.getAllGoals().isEmpty());
    }

    // testing delete goal
    @Test
    public void validateAllGoalsSizeMultipleWithAllDeleted(){
        for (int i = 1; i < 11; i++){
            goalService.newGoal(description + i, ad);
            assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
        }
        goalService.deleteGoal(goalService.getAllGoals().get(0).getId());
        assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals should be 10", goalService.getAllGoals().size() == 9);
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSizeFail(){
        goalService.newGoal(description, ad);
        assertFalse("validateAllGoalsSize: Should fail with the size of all goals being 1, not 2", goalService.getAllGoals().size() == 2);
    }


    // testing get all goals
    @Test
    public void validateAllGoalsSpecificGoalDesc(){
        goalService.newGoal(description, ad);
        assertTrue("validateAllGoalsSpecificGoalDesc: Should pass with the description of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getDescription() == description);
    }


    // testing get all goals
    @Test
    public void validateAllGoalsSpecificGoalAdId(){
        goalService.newGoal(description, ad);
        assertTrue("validateAllGoalsSpecificGoalAdId: Should pass with the ad id of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getAd_id() == ad.getId());
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSpecificGoalAdIdFail(){
        goalService.newGoal(description, ad);
        assertFalse("validateAllGoalsSpecificGoalAdId: Should fail with the ad id of the goal not being equal to the 100", goalService.getAllGoals().get(0).getAd_id() == 100);
    }

    // testing get individual goals
    @Test
    public void validateGetIndividualGoal(){
        goalService.newGoal(description, ad);
        assertTrue("validateGetIndividualGoals: Should pass with the individual goal being equal to the goal that was set", goalService.getIndividualGoals(ad.getId()).get(0) == goalService.getAllGoals().get(0));
    }

    // testing get individual goals
    @Test
    public void validateGetIndividualGoalFail(){
        goalService.newGoal(description, ad);
        assertFalse("validateGetIndividualGoalFail: Should fail with the individual goal not being equal to null", goalService.getIndividualGoals(ad.getId()).get(0) == null);
    }

    // testing new goal
    @Test
    public void validateNewGoalSizeDuplicate(){
        goalService.newGoal(description, ad);
        goalService.newGoal(description, ad);
        assertTrue("validateAllGoalsSize: Should pass size of goals being 2", goalService.getAllGoals().size() == 2);
    }

    // testing delete all goals
    @Test
    public void validateDeleteAllGoals(){
        goalService.newGoal(description, ad);
        goalService.deleteAllGoals();
        assertTrue("validateDeleteAllGoals: Should pass with size being 0", goalService.getAllGoals().size() == 0);
    }

    // testing delete all goals
    @Test
    public void validateDeleteAllGoalsMultiple(){
        goalService.newGoal(description, ad);
        goalService.newGoal(description, ad);
        goalService.deleteAllGoals();
        assertTrue("validateDeleteAllGoalsMultiple: Should pass with size being 0", goalService.getAllGoals().size() == 0);
    }

}
