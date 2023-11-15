package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;

import java.util.List;

/**
 * Interface for advertisement services. Contains all business methods for dealing with advertisements.
 *
 * @author Ryan Johnson, Hank Rugg
 */
public interface AdService {

    /**
     * Creates a new advertisement and adds it to the database.
     *
     * @param name        name of new ad
     * @param description description of new ad
     * @param price       price of new ad
     * @return True if ad successfully added to the database; False otherwise
     */
    boolean createAd(String name, String description, Float price, int user_id);

    /**
     * Changes the data for the specified advertisement
     *
     * @param name        String representing the desired name of the advertisement
     * @param description String representing the desired description of the advertisement
     * @param price       Float representing the desired price for the advertisement
     * @param id          Integer representing the ID of the advertisement to be changed
     * @return true if the designated advertisement is edited successfully; false otherwise
     */
    boolean editAd(String name, String description, Float price, Integer id, int user_id);

    /**
     * Places a designated advertisement onto the list of starred advertisements.
     *
     * @param id Integer representing the ID of the Ad object to be added to the list of starred advertisements
     * @return Ad object being added to the list of starred advertisements
     */
    boolean starAd(Integer id);

    /**
     * Removes a designated advertisement from the list of starred ads.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the list of saved ads
     * @return true if the designated Ad object is successfully removed from the list of starred ads; false otherwise
     */
    boolean removeStarredAd(Integer id);

    /**
     * Removes a designated advertisement from the database.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the database
     * @return true if the Ad object with the designated ID is removed from the database; false otherwise
     */
    boolean deleteAd(Integer id);

    /**
     * Returns a list of all advertisements in the database.
     *
     * @return List of every Ad object in the database
     */
    List<Ad> loadAllAds();

    /**
     * Returns a list of all starred advertisements.
     *
     * @return List of all Ad objects with a "starred" status of True
     */
    List<Ad> loadStarredAds();

    /**
     * Loads all the ads created by the specified user ID.
     *
     * @return List of Ads created by the current user
     */
    List<Ad> loadCreatedAds(Integer id);

    /**
     * Returns the Ad object with the specified ID
     *
     * @param id Integer representing the ID number of the desired Ad object
     * @return Ad object with the specified ID
     */
    Ad getReferenceById(Integer id);
}