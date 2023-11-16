package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @return true if ad successfully added to the database; false otherwise
     */
    public boolean createAd(String name, String description, float price, User user) {
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
     * @param user        User object that created the ad
     * @return true if the designated advertisement is edited successfully; false otherwise
     */
    public boolean editAd(String name, String description, float price, int id, User user) {
        // Edits an advertisement to the DB only if the name field has been filled, and there are no null values
        if (name == null || name.isEmpty()) {
            log.debug("Attempt to edit ad unsuccessful due to invalid credentials");
            return false;
        }
        // Makes sure an advertisement with the designated id exits before attempting to edit it
        if (!adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to edit ad due to invalid ID");
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
     * Removes a designated advertisement from the database.
     *
     * @param id int representing the ID of the Ad object to be removed from the database
     * @return true if the Ad object with the designated ID is removed from the database; false otherwise
     */
    public boolean deleteAd(int id) {
        if (!adRepo.existsById(id)) {
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
        List<Ad> allAds = adRepo.findAll();
        log.debug("Loading all Ads: " + allAds.size() + " ad(s) loaded");
        return allAds;
    }

    /**
     * Loads all the ads created by the specified user ID.
     *
     * @return List of Ads created by the current user
     */
    public List<Ad> loadCreatedAds(int id) {
        if (!adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to delete ad due to invalid ID");
            return new ArrayList<>();
        }
        List<Ad> createdAds = adRepo.findByUserId(id);
        log.debug("Loading all Ads Created by User #" + id + ": " + createdAds.size() + " ad(s) loaded");
        return createdAds;
    }

    /**
     * Returns the Ad object with the specified ID
     *
     * @param id int representing the ID number of the desired Ad object
     * @return Ad object with the specified ID
     */
    @Override
    public Ad getReferenceById(int id) {
        if (!adRepo.existsById(id)) {
            log.debug("Unsuccessful attempt to delete ad due to invalid ID");
            return new Ad();
        }
        Ad selectedAd = adRepo.getReferenceById(id);
        log.debug("Getting Ad #" + id + ": " + selectedAd);
        return selectedAd;
    }
}