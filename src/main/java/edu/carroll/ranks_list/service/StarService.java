package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;

import java.util.List;

/**
 * Interface for star services. Contains all business methods for checking status of stars.
 *
 * @author Ryan Johnson, Hank Rugg
 */
public interface StarService {
    /**
     * Determines if a designated advertisement is starred by a given user.
     *
     * @param adId Integer representing the ID of the advertisement to be checked
     * @param userId Integer representing the ID of the current user
     * @return
     */
    boolean isAdStarred(Integer adId, Integer userId);

    /**
     * Toggles the status of whether a star is starred.
     *
     * @param adId Integer representing the ID of the ad being starred/unstarred
     * @param userId Integer representing the ID of the current user starring/unstarring the advertisement
     *
     * @return true if the starred status of the star is successfully toggled; false otherwise
     */
    boolean changeStarStatus(Integer adId, Integer userId);

    /**
     * Returns a list of all starred advertisements.
     *
     * @return List of all Ad objects with a "starred" status of True
     */
    List<Ad> loadStarredAds(Integer userId);
}
