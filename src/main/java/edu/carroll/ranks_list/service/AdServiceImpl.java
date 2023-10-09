package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import edu.carroll.ranks_list.repository.UserRepository;
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
    public AdServiceImpl(AdRepository adRepo, UserRepository userRepo) {
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
    @Override
    public boolean createAd(String name, String description, Float price, Integer userId) {
        // Adds an advertisement to the DB only if all fields have been filled
        if (name != null && description != null && price != null) {
            Ad newAd = new Ad(name, price, description, userId);
            log.info("New Ad: " + newAd);
            adRepo.save(newAd);
            return true;
        }
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
     * Returns a list of all saved advertisements.
     *
     * @return List of all Ad objects with a "saved" status of True
     */
    public List<Ad> loadStarredAds() {
        List<Ad> starredAds = adRepo.findByStarred(true);
        return starredAds;
    }

    /**
     * Removes a designated advertisement from the database.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the database
     * @return Ad object removed from the database
     */
    public Ad deleteAd(Integer id) {
        Ad deletedAd = adRepo.getReferenceById(id);
        adRepo.deleteById(id);
        log.info("Advertisement #{id} deleted");

        return deletedAd;
    }

    /**
     * Testing class that removes all advertisements from the database.
     */
    public void deleteAllAds() {
        adRepo.deleteAll();
    }

    /**
     * Removes a designated advertisement from the list of starred ads.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the list of saved ads
     * @return Ad object removed from the list of saved advertisements
     */
    public Ad removeStarredAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        if (changedAd != null) {
            changedAd.setStarred(false);
            adRepo.save(changedAd);
            log.info("Advertisement #{id} removed from saved ads");
        }
        return changedAd;
    }

    /**
     * Removes all ads from the list of starred ads.
     *
     * @return True if ads were successfully removed from the starred list; False otherwise
     */
    public boolean removeAllStarredAds() {
        List<Ad> starredAds = loadStarredAds();
        for (Ad ad : starredAds) {
            ad.setStarred(false);
            adRepo.save(ad);
        }
        return true;
    }

    /**
     * Places a designated advertisement onto the list of starred advertisements.
     *
     * @param id Integer representing the ID of the Ad object to be added to the list of starred advertisements
     * @return Ad object being added to the list of starred advertisements
     */
    public Ad starAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setStarred(true);
        adRepo.save(changedAd);

        return changedAd;
    }


    /**
     * Loads all the ads created by the specified user ID.
     *
     * @return List of Ads created by the current user
     */
    public List<Ad> loadCreatedAds(Integer id) {
        return adRepo.findByUserId(id);
    }

    @Override
    public List<Ad> getAllAds() {
        return adRepo.findAll();
    }
}
