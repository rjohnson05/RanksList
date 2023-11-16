package edu.carroll.ranks_list.repository;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Star;
import edu.carroll.ranks_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for stars.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Repository
public interface StarRepository extends JpaRepository<Star, Integer> {

    /**
     * Returns a list of Star objects belonging to the specified advertisement.
     *
     * @param ad Ad object the star belongs to
     * @return List of star object belonging to the specified Ad object
     */
    List<Star> getReferenceByAd(Ad ad);

    /**
     * Returns a list of Star objects belonging to the specified user.
     *
     * @param user User object the star belongs to
     * @return List of star object belonging to the specified User object
     */
    List<Star> getReferenceByUser(User user);
}
