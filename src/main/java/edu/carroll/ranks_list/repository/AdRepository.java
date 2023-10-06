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
    public List<Ad> findByStarred(Boolean starred);

    /**
     * Returns a list of all advertisements containing the designated name.
     *
     * @param name String designating the name to be searched for
     * @return List of Ad objects with the indicated name
     */
    public List<Ad> findByName(String name);
}
