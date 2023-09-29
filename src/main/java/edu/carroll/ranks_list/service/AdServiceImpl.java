package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private List<String> goalList = new ArrayList<String>();


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
     * @param name name of new ad
     * @param description description of new ad
     * @param price price of new ad
     */
    @Override
    public boolean createAd(String name, String description, Float price) {
        Ad newAd = new Ad(name, price, description);
        log.info("New Ad: " + newAd);
        adRepo.save(newAd);
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
     * Returns a list of all saved advertisements.
     *
     * @return List of all Ad objects with a "saved" status of True
     */
    public List<Ad> loadSavedAds() {
        List<Ad> savedAds = adRepo.findBySaved(true);
        return savedAds;
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
     * Removes a designated advertisement from the list of saved ads.
     *
     * @param id Integer representing the ID of the Ad object to be removed from the list of saved ads
     * @return Ad object removed from the list of saved advertisements
     */
    public Ad removeSavedAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setSaved(false);
        adRepo.save(changedAd);
        log.info("Advertisement #{id} removed from saved ads");

        return changedAd;
    }

    /**
     * Places a designated advertisement onto the list of saved advertisements.
     *
     * @param id Integer representing the ID of the Ad object to be added to the list of saved advertisements
     * @return Ad object being added to the list of saved advertisements
     */
    public Ad saveAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setSaved(true);
        adRepo.save(changedAd);

        return changedAd;
    }

    @Override
    public List<Ad> getAllAds() {
        return adRepo.findAll();
    }



}

