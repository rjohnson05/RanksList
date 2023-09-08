package edu.carroll.RanksList.jpa.repo;

import edu.carroll.RanksList.jpa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepository extends JpaRepository<User, Integer>{
    // JPA throws an exception if we attempt to return a single object that doesn't exist, so return a list
    // even though we only expect either an empty list of a single element.
    List<User> findByUsernameIgnoreCase(String username);
}