package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Goal;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertTrue;

@SpringBootTest
@Transactional
public class GoalServiceImplTest {

    private static final String name = "TestGoal";

    private static final String description = "Testing the goals";

    private static final Integer ad_id = 0;

    @Autowired
    private GoalService goalService;

    private Goal fakeGoal = new Goal(name, description, ad_id);

    @BeforeEach
    public void beforeTest() {
        goalService.deleteAllGoals();
    }

    @Test
    public void validateNewGoalSuccess(){
        assertTrue("validateAllGoalsSize: Should pass with a new goal created returning true", goalService.newGoal(name, description, ad_id));
    }


    @Test
    public void validateNewGoalFail(){
        goalService.newGoal(name, description, ad_id);
        assertTrue("validateAllGoalsSize: Should pass with a new goal created returning true", goalService.getAllGoals().size() == 2);
    }


}
