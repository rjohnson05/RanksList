package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
     * @param user        User object that created the ad
     * @return True if ad successfully added to the database; False otherwise
     */
    public boolean createAd(String name, String description, Float price, User user) {
        // Adds an advertisement to the DB only if the name field has been filled, and there are no null values
        if (name == null || name.isEmpty() || user == null) {
            log.debug("Advertisement not created due to invalid data");
            return false;
        }
        // Make sure the user hasn't already tried creating this ad
        for (Ad ad : loadCreatedAds(user.getId())) {
            if (ad.getName().equals(name)) {
                log.debug("User tried creating advertisement with duplicate name");
                return false;
            }
        }
        Ad newAd = new Ad(name, price, description, user);
        adRepo.save(newAd);
        log.info("Ad #" + newAd.getId() + " created");
        log.debug("New Ad: " + newAd);
        return true;
    }

    /**
     * Changes the data for the specified advertisement
     *
     * @param name        String representing the desired name of the advertisement
     * @param description String representing the desired description of the advertisement
     * @param price       Float representing the desired price for the advertisement
     * @param id          Integer representing the ID of the advertisement to be changed
     * @paraam user       User object that created the ad
     * @return true if the designated advertisement is edited successfully; false otherwise
     */
    public boolean editAd(String name, String description, Float price, Integer id, User user) {
        // Edits an advertisement to the DB only if the name field has been filled, and there are no null values
        if (name == null || name.isEmpty() || price == null || description == null || id == null) {
            log.debug("Attempt to edit ad unsuccessful due to null field or empty name");
            return false;
        }
        // Make sure the ad data was changed somehow
        Ad selectedAd = adRepo.getReferenceById(id);
        if (Objects.equals(selectedAd.getName(), name) && Objects.equals(selectedAd.getPrice(), price) && Objects.equals(selectedAd.getDescription(), description)) {
            log.debug("Attempt to edit ad unsuccessful due to no changes being made");
            return false;
        }
        // Make sure the editor is the creator of the ad
        if (!loadCreatedAds(user.getId()).contains(selectedAd)) {
            log.debug("Attempt to edit ad unsuccessful since would-be editor was not the creator");
            return false;
        }
        // Make sure the user hasn't already tried creating this ad
        for (Ad ad : loadCreatedAds(user.getId())) {
            if (ad.getName().equals(name) && ad.getId() != id) {
                log.debug("User tried creating advertisement with duplicate name");
                return false;
            }
        }
        // Makes sure an advertisement with the designated id exits before attempting to edit it
        if (!adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to edit ad due to invalid ID");
            return false;
        }
        Ad changingAd = adRepo.getReferenceById(id);
        log.debug("Changing Ad: " + changingAd);
        changingAd.setName(name);
        changingAd.setDescription(description);
        changingAd.setPrice(price);
        adRepo.save(changingAd);
        log.info("Ad #" + id + " edited");
        log.debug("Ad edited: " + changingAd);
        return true;
    }

    /**
     * Places a designated advertisement onto the list of starred advertisements.
     *
     * @param id Integer representing the ID of the Ad object to be added to the list of starred advertisements
     * @return Ad object being added to the list of starred advertisements
     */
    public boolean starAd(Integer id) {
        // Only stars the ad if an ad with the designated id exists
        if (id == null || !adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        // Doesn't star the ad if it is already starred
        if (adRepo.getReferenceById(id).getStarred()) {
            log.debug("Unsuccessful attempt to star ad; was already starred");
            return false;
        }
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setStarred(true);
        adRepo.save(changedAd);
        log.info("Starred Ad #" + id);
        return true;
    }

    /**
     * Removes a designated advertisement from the list of starred ads.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the list of saved ads
     * @return true if the designated Ad object is successfully removed from the list of starred ads; false otherwise
     */
    public boolean removeStarredAd(Integer id) {
        if (id == null || !adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to unstar ad due to invalid ID");
            return false;
        }
        if (!adRepo.getReferenceById(id).getStarred()) {
            log.debug("Unsuccessful attempt to unstar ad; was already unstarred");
            return false;
        }
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setStarred(false);
        adRepo.save(changedAd);
        log.info("Advertisement #{id} was unstarred");
        return true;
    }

    /**
     * Removes a designated advertisement from the database.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the database
     * @return true if the Ad object with the designated ID is removed from the database; false otherwise
     */
    public boolean deleteAd(Integer id) {
        if (id == null || !adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to delete ad due to invalid ID");
            return false;
        }
        adRepo.deleteById(id);
        log.info("Advertisement #{id} deleted");
        return true;
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