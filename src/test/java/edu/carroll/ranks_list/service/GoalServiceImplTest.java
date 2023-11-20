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

    private static final String description = "Testing the goals";


    @Autowired
    private GoalService goalService;

    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeTest() {
        userService.createUser("Username1", "Password@1");
        int user_id = userService.findByUsernameIgnoreCase("Username1").get(0).getId();
        adService.createAd("Name of Ad", "Description of ad", 10.00F, user_id);
    }

    // testing new goal method
    @Test
    public void validateNewGoalSuccess(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        int size = goalService.getAllGoals().size();
        assertTrue("validateNewGoalSuccess: Should pass with a new goal created returning true", goalService.newGoal(description, ad, user_id));
        List<Goal> individualGoals = goalService.getIndividualGoals(ad.getId(), ad.getUser().getId());
        assertEquals("validateNewGoalSuccess: Newly added goal did not increase number of goals for ad", individualGoals.size(), size+1);
        if (individualGoals.size() == 1){
            assertTrue("validateNewGoalSuccess: Set description of goal does not match fetched goal", individualGoals.get(0).getDescription().equals(description));
        }
    }


    // testing new goal method
    @Test
    public void validateNewGoalNullDesc(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalNullDesc: Should fail with a new goal not created", goalService.newGoal(null, ad, user_id));
        assertTrue("validateNewGoalNullDesc: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

    // testing new goal method
    @Test
    public void validateNewGoalNullId(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        int size = goalService.getAllGoals().size();
        assertFalse("validateNewGoalNullId: Should fail with a new goal not created", goalService.newGoal(description, null, user_id));
        assertTrue("validateNewGoalNullId: A goal was added to the list when it shouldn't have been", size == goalService.getAllGoals().size());
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSize(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertTrue("validateAllGoalsSize: Should pass with the size of all goals being 1", goalService.getAllGoals().size() == 1);
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSizeMultiple(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        for (int i = 1; i < 11; i++){
            goalService.newGoal(description, ad, user_id);
            assertTrue("validateAllGoalsSizeMultiple: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
        }
        assertTrue("validateAllGoalsSizeMultiple: The size of the list of goals should be 10", goalService.getAllGoals().size() == 10);

    }

    // testing get all goals
    @Test
    public void validateAllGoalsSizeMultipleWithDeletion(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        for (int i = 1; i < 11; i++){
            goalService.newGoal(description, ad, user_id);
            assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
        }
        int size = goalService.getAllGoals().size();
        goalService.deleteGoal(goalService.getAllGoals().get(0).getId());
        assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals should be 9", goalService.getAllGoals().size() == size-1);
    }

    // testing delete goal
    @Test
    public void deleteSpecificGoal(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertTrue("deleteSpecificGoal: The size of the list of goals should be 1", goalService.getAllGoals().size() == 1);
        goalService.deleteGoal(goalService.getAllGoals().get(0).getId());
        assertTrue("deleteSpecificGoal: The size of the list of goals should be 9", goalService.getAllGoals().isEmpty());
    }

    // testing delete goal
    @Test
    public void validateAllGoalsSizeMultipleWithAllDeleted(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        for (int i = 1; i < 11; i++){
            goalService.newGoal(description + i, ad, user_id);
            assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals does not match the amount of goals added", goalService.getAllGoals().size() == i);
        }
        goalService.deleteGoal(goalService.getAllGoals().get(0).getId());
        assertTrue("validateAllGoalsSizeMultipleWithDeletion: The size of the list of goals should be 10", goalService.getAllGoals().size() == 9);
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSizeFail(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertFalse("validateAllGoalsSize: Should fail with the size of all goals being 1, not 2", goalService.getAllGoals().size() == 2);
    }


    // testing get all goals
    @Test
    public void validateAllGoalsSpecificGoalDesc(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertTrue("validateAllGoalsSpecificGoalDesc: Should pass with the description of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getDescription() == description);
    }


    // testing get all goals
    @Test
    public void validateAllGoalsSpecificGoalAdId(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertTrue("validateAllGoalsSpecificGoalAdId: Should pass with the ad id of the goal being equal to what it was set to", goalService.getAllGoals().get(0).getAd().getId() == ad.getId());
    }

    // testing get all goals
    @Test
    public void validateAllGoalsSpecificGoalAdIdFail(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertFalse("validateAllGoalsSpecificGoalAdId: Should fail with the ad id of the goal not being equal to the 100", goalService.getAllGoals().get(0).getAd().getId() == 100);
    }

    // testing get individual goals
    @Test
    public void validateGetIndividualGoal(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertTrue("validateGetIndividualGoals: Should pass with the individual goal being equal to the goal that was set", goalService.getIndividualGoals(ad.getId(), user_id).get(0) == goalService.getAllGoals().get(0));
    }

    // testing get individual goals
    @Test
    public void validateGetIndividualGoalFail(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        assertFalse("validateGetIndividualGoalFail: Should fail with the individual goal not being equal to null", goalService.getIndividualGoals(ad.getId(), user_id).get(0) == null);
    }

    // testing new goal
    @Test
    public void validateNewGoalSizeDuplicate(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        goalService.newGoal(description, ad, user_id);
        assertTrue("validateAllGoalsSize: Should pass size of goals being 2", goalService.getAllGoals().size() == 2);
    }

    // testing delete all goals
    @Test
    public void validateDeleteAllGoals(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        goalService.deleteAllGoals();
        assertTrue("validateDeleteAllGoals: Should pass with size being 0", goalService.getAllGoals().size() == 0);
    }

    // testing delete all goals
    @Test
    public void validateDeleteAllGoalsMultiple(){
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        Ad ad = adService.loadAllAds().get(0);
        goalService.newGoal(description, ad, user_id);
        goalService.newGoal(description, ad, user_id);
        goalService.deleteAllGoals();
        assertTrue("validateDeleteAllGoalsMultiple: Should pass with size being 0", goalService.getAllGoals().size() == 0);
    }

}
