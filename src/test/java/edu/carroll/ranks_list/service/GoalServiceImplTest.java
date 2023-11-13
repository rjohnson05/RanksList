package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class GoalServiceImplTest {

    // goal name
    private static final String name = "Test";

    private static final String description = "Testing the goals";

    private final User user = new User("username", "password");

    private final Ad ad = new Ad("AdName", 10.00F, "This is the description", user);

    @Autowired
    private GoalService goalService;

    @BeforeEach
    public void beforeTest() {
        goalService.deleteAllGoals();
    }

    @Test
    public void validateNewGoalSuccess(){
        int size = goalService.getAllGoals().size();
        assertTrue("validateNewGoalSuccess: Should pass with a new goal created returning true", goalService.newGoal(name, description, ad));
        List<Goal> individualGoals = goalService.getIndividualGoals(ad.getId());
        assertEquals("validateNewGoalSuccess: Newly added goal did not increase number of goals for ad", individualGoals.size(), size+1);
        if (individualGoals.size() == 1){
            assertTrue("validateNewGoalSuccess: Set name of goal does not match fetched goal", individualGoals.get(0).getName().equals(name));
            assertTrue("validateNewGoalSuccess: Set description of goal does not match fetched goal", individualGoals.get(0).getDescription().equals(description));
        }
    }
    @Test
    public void validateNewGoalNullName(){
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalFail: Should fail with a new goal not created", goalService.newGoal(null, description, ad));
        assertTrue("validateNewGoalFail: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

    @Test
    public void validateNewGoalNullDesc(){
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalNullDesc: Should fail with a new goal not created", goalService.newGoal(name, null, ad));
        assertTrue("validateNewGoalNullDesc: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

    @Test
    public void validateNewGoalNullId(){
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalNullId: Should fail with a new goal not created", goalService.newGoal(name, description, null));
        assertTrue("validateNewGoalNullId: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

//    @Test
//    public void validateAllGoalsSize(){
//        goalService.newGoal(name, description, ad);
//        assertTrue("validateAllGoalsSize: Should pass with the size of all goals being 1", goalService.getAllGoals().size() == 1);
//    }

//    @Test
//    public void validateAllGoalsSizeMultiple(){
//        for (int i = 1; i < 11; i++){
//            goalService.newGoal(name, description, ad);
//            assertTrue("validateAllGoalsSizeMultiple: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
//        }
//        assertTrue("validateAllGoalsSizeMultiple: The size of the list of goals should be 10", goalService.getAllGoals().size() == 10);
//
//    }

//    @Test
//    public void validateAllGoalsSizeMultipleWithDeletion(){
//        for (int i = 1; i < 11; i++){
//            goalService.newGoal(name, description, ad);
//            assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
//        }
//        int size = goalService.getAllGoals().size();
//        goalService.deleteGoal(5);
//        System.out.println(size);
//        System.out.println(goalService.getAllGoals().size());
//        assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals should be 9", goalService.getAllGoals().size() == size-1);
//    }

//    @Test
//    public void deleteSpecificGoal(){
//        goalService.newGoal(name, description, ad);
//        assertTrue("deleteSpecificGoal: The size of the list of goals should be 1", goalService.getAllGoals().size() == 1);
//        System.out.println(goalService.getAllGoals().size());
//        goalService.deleteGoal(ad);
//        System.out.println(goalService.getAllGoals().size());
//        assertTrue("deleteSpecificGoal: The size of the list of goals should be 9", goalService.getAllGoals().size() == 0);
//    }

//    @Test
//    public void validateAllGoalsSizeMultipleWithAllDeleted(){
//        for (int i = 1; i < 11; i++){
//            goalService.newGoal(name, description, i);
//            assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
//        }
//        goalService.deleteGoal(5);
//        assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals should be 10", goalService.getAllGoals().size() == 9);
//    }


//    @Test
//    public void validateAllGoalsSizeFail(){
//        goalService.newGoal(name, description, ad);
//        assertFalse("validateAllGoalsSize: Should fail with the size of all goals being 1, not 2", goalService.getAllGoals().size() == 2);
//    }


//    @Test
//    public void validateAllGoalsSpecificGoalDesc(){
//        goalService.newGoal(name, description, ad);
//        assertTrue("validateAllGoalsSpecificGoalDesc: Should pass with the description of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getDescription() == description);
//    }

//    @Test
//    public void validateAllGoalsSpecificGoalDescFail(){
//        goalService.newGoal(name, description, ad);
//        assertFalse("validateAllGoalsSpecificGoalDesc: Should fail with the description of the goal not being equal to the name", goalService.getAllGoals().get(0).getDescription() == name);
//    }

//    @Test
//    public void validateAllGoalsSpecificGoalAdId(){
//        goalService.newGoal(name, description, ad);
//        assertTrue("validateAllGoalsSpecificGoalAdId: Should pass with the ad id of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getAd_id() == ad);
//    }

//    @Test
//    public void validateAllGoalsSpecificGoalAdIdFail(){
//        goalService.newGoal(name, description, ad);
//        assertFalse("validateAllGoalsSpecificGoalAdId: Should fail with the ad id of the goal not being equal to the 100", goalService.getAllGoals().get(0).getAd_id() == 100);
//    }

//    @Test
//    public void validateGetIndividualGoal(){
//        goalService.newGoal(name, description, ad);
//        assertTrue("validateGetIndividualGoals: Should pass with the individual goal being equal to the goal that was set", goalService.getIndividualGoals(adId).get(0) == goalService.getAllGoals().get(0));
//    }

//    @Test
//    public void validateGetIndividualGoalFail(){
//        goalService.newGoal(name, description, ad);
//        assertFalse("validateGetIndividualGoalFail: Should fail with the individual goal not being equal to null", goalService.getIndividualGoals(adId).get(0) == null);
//    }

//    @Test
//    public void validateNewGoalSizeDuplicate(){
//        goalService.newGoal(name, description, ad);
//        goalService.newGoal(name, description, ad);
//        assertTrue("validateAllGoalsSize: Should pass size of goals being 2", goalService.getAllGoals().size() == 2);
//    }

//    @Test
//    public void validateDeleteAllGoals(){
//        goalService.newGoal(name, description, ad);
//        goalService.deleteAllGoals();
//        assertTrue("validateDeleteAllGoals: Should pass with size being 0", goalService.getAllGoals().size() == 0);
//    }

//    @Test
//    public void validateDeleteAllGoalsMultiple(){
//        goalService.newGoal(name, description, ad);
//        goalService.newGoal(name, description, ad);
//        goalService.deleteAllGoals();
//        assertTrue("validateDeleteAllGoalsMultiple: Should pass with size being 0", goalService.getAllGoals().size() == 0);
//    }



}
