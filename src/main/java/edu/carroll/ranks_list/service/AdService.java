package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;

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
    boolean createAd(String name, String description, Float price, User user);

    /**
     * Changes the data for the specified advertisement
     *
     * @param name        String representing the desired name of the advertisement
     * @param description String representing the desired description of the advertisement
     * @param price       Float representing the desired price for the advertisement
     * @param id          Integer representing the ID of the advertisement to be changed
     * @return true if the designated advertisement is edited successfully; false otherwise
     */
    boolean editAd(String name, String description, Float price, Integer id, User user);

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