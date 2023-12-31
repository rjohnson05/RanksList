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
     * @param name        String containing the name of new ad
     * @param description String containing the description of new ad
     * @param price       float containing the price of new ad
     * @param user_id     user_id object creating the advertisement
     * @return true if ad successfully added to the database; false otherwise
     */
    boolean createAd(String name, String description, Float price, int user_id);

    /**
     * Changes the data for the specified advertisement
     *
     * @param name        String representing the desired name of the advertisement
     * @param description String representing the desired description of the advertisement
     * @param price       float representing the desired price for the advertisement
     * @param id          int representing the ID of the advertisement to be changed
     * @param user_id     user_id object creating the advertisement
     * @return true if the designated advertisement is edited successfully; false otherwise
     */
    boolean editAd(String name, String description, Float price, Integer id, int user_id);


    /**
     * Removes a designated advertisement from the database.
     *
     * @param id int representing the ID of the Ad object to be removed from the database
     * @return true if the Ad object with the designated ID is removed from the database; false otherwise
     */
    boolean deleteAd(int id);

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
    List<Ad> loadCreatedAds(int id);

    /**
     * Returns the Ad object with the specified ID
     *
     * @param id int representing the ID number of the desired Ad object
     * @return Ad object with the specified ID
     */
    Ad getReferenceById(int id);
}