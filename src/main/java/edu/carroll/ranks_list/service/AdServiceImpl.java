package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for advertisements. Contains all business logic for dealing with advertisements.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@Service
public class AdServiceImpl implements AdService {

    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    private final AdRepository adRepo;

    /**
     * Constructor for the Ad Service, creating the service with an Ad Repo.
     *
     * @param adRepo Repository for advertisements
     */
    public AdServiceImpl(AdRepository adRepo) {
        this.adRepo = adRepo;
    }

    /**
     * Creates a new advertisement and adds it to the database.
     *
     * @param name        name of new ad
     * @param description description of new ad
     * @param price       price of new ad
     * @return True if ad successfully added to the database; False otherwise
     */
    public boolean createAd(String name, String description, Float price, User user) {
        // Adds an advertisement to the DB only if all fields have been filled
        if (name != null && !name.isEmpty() && description != null && price != null) {
            Ad newAd = new Ad(name, price, description, user);
            log.info("New Ad: " + newAd);
            adRepo.save(newAd);
            return true;
        }
        log.info("Invalid credentials used in attempting to create an advertisement");
        return false;
    }

    /**
     * Changes the data for the specified advertisement
     *
     * @param name        String representing the desired name of the advertisement
     * @param description String representing the desired description of the advertisement
     * @param price       Float representing the desired price for the advertisement
     * @param id          Integer representing the ID of the advertisement to be changed
     * @return true if the designated advertisement is edited successfully; false otherwise
     */
    public boolean editAd(String name, String description, Float price, Integer id) {
        if (name != null && price != null && description != null && id != null) {
            Ad changingAd = adRepo.getReferenceById(id);
            changingAd.setName(name);
            changingAd.setDescription(description);
            changingAd.setPrice(price);
            adRepo.save(changingAd);
            log.info("Ad #" + id + " edited: " + changingAd);
            return true;
        }
        log.debug("Unsuccessfully attempted to edit Ad #" + id);
        return false;
    }

    /**
     * Places a designated advertisement onto the list of starred advertisements.
     *
     * @param id Integer representing the ID of the Ad object to be added to the list of starred advertisements
     * @return Ad object being added to the list of starred advertisements
     */
    public boolean starAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        if (changedAd != null) {
            changedAd.setStarred(true);
            adRepo.save(changedAd);
            log.info("Starred Ad #" + id);
            return true;
        }
        log.debug("Unsuccessfully attempted to star Ad #" + id);
        return false;
    }

    /**
     * Removes a designated advertisement from the list of starred ads.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the list of saved ads
     * @return true if the designated Ad object is successfully removed from the list of starred ads; false otherwise
     */
    public boolean removeStarredAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        if (changedAd != null) {
            changedAd.setStarred(false);
            adRepo.save(changedAd);
            log.info("Advertisement #{id} was unstarred");
            return true;
        }
        log.debug("Attempted to unstar Ad#" + id + " unsuccessfully");
        return false;
    }

    /**
     * Removes a designated advertisement from the database.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the database
     * @return true if the Ad object with the designated ID is removed from the database; false otherwise
     */
    public boolean deleteAd(Integer id) {
        Ad selectedAd = adRepo.getReferenceById(id);
        if (selectedAd != null) {
            adRepo.deleteById(id);
            log.info("Advertisement #{id} deleted");
            return true;
        }
        log.info("Attempted Ad Deletion with Invalid ID #" + id);
        return false;
    }

    /**
     * Returns a list of all advertisements in the database.
     *
     * @return List of every Ad object in the database
     */
    public List<Ad> loadAllAds() {
        return adRepo.findAll();
    }

    /**
     * Returns a list of all starred advertisements.
     *
     * @return List of all Ad objects with a "starred" status of True
     */
    public List<Ad> loadStarredAds() {
        return adRepo.findByStarred(true);
    }

    /**
     * Loads all the ads created by the specified user ID.
     *
     * @return List of Ads created by the current user
     */
    public List<Ad> loadCreatedAds(Integer id) {
        return adRepo.findByUserId(id);
    }

    /**
     * Returns the Ad object with the specified ID
     *
     * @param id Integer representing the ID number of the desired Ad object
     * @return Ad object with the specified ID
     */
    @Override
    public Ad getReferenceById(Integer id) {
        return adRepo.getReferenceById(id);
    }
}