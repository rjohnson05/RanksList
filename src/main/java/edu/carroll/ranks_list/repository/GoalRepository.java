package edu.carroll.ranks_list.repository;

import edu.carroll.ranks_list.model.Goal;
import edu.carroll.ranks_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for goals.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Repository
public interface GoalRepository extends JpaRepository<Goal, Integer> {
    List<Goal> findByadId(Integer adId);


}
