package edu.carroll.ranks_list.repository;

import edu.carroll.ranks_list.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for users.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Returns a list of all users with the designated username. This list will have a length of either 0 if there is no user
     * with the designated username or 1 if there is a user with the designated username.
     *
     * @param username String object containing the username
     * @return List of User objects that have the designated username
     */
    List<User> findByUsernameIgnoreCase(String username);

    /**
     * Returns the User object with the specified ID.
     *
     * @param id Integer representing the ID of the desired User object
     * @return User object with the specified ID
     */
    User getReferenceById(Integer id);
}