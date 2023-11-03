package edu.carroll.ranks_list.repository;

import edu.carroll.ranks_list.model.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for advertisements.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {
    /**
     * Returns a list of all advertisements containing the designated "saved" boolean status.
     *
     * @param starred True if advertisement is saved; False otherwise
     * @return List of Ad objects with the indicated "saved" status
     */
    List<Ad> findByStarred(Boolean starred);

    /**
     * Returns a list of all advertisements containing the designated name.
     *
     * @param user_id Integer representing the ID of the specified user
     * @return List of Ad objects created by the specified user
     */
    List<Ad> findByUserId(Integer user_id);

    /**
     * Returns the ad with the matching ID.
     *
     * @param id Integer representing the ID of the specified ad
     * @return Ad object with the specified ID
     */
    Ad getReferenceById(Integer id);
}