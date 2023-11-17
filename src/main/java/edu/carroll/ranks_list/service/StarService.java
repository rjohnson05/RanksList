package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Star;

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
     * @param adId   int representing the ID of the advertisement to be checked
     * @param userId int representing the ID of the current user
     * @return true if the ad is starred by the current user; false otherwise
     */
    boolean isAdStarred(int adId, int userId);

    /**
     * Toggles the status of whether a star is starred.
     *
     * @param adId   int representing the ID of the ad being starred/unstarred
     * @param userId int representing the ID of the current user starring/unstarring the advertisement
     * @return true if the starred status for the specified ad is successfully changed; false otherwise
     */
    boolean changeStarStatus(int adId, int userId);

    /**
     * Returns a list of all starred advertisements.
     *
     * @param userId int representing the ID of the current user starring/unstarring the advertisement
     * @return List of all Ad objects with a "starred" status of True
     */
    List<Ad> loadStarredAds(int userId);
}
