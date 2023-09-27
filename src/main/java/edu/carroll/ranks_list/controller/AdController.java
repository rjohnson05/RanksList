package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.service.AdService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * API for HTTP requests regarding advertisements. Specifies how to handle HTTP requests that involve advertisement data.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@RestController
@CrossOrigin("http://localhost:3000")
public class AdController {
    private static final Logger log = LoggerFactory.getLogger(AdController.class);
    private final AdService adService;

    /**
     * Constructor for the Ad Controller. Creates a service for the advertisements business logic.
     *
     * @param adService contains all logic related to advertisement data
     */
    public AdController(AdService adService) {
        this.adService = adService;
    }

    /**
     * Creates a new advertisement and adds it to the database.
     *
     * @param newAd Ad to be added to the database
     * @return the Ad successfully added to the database
     */
    @PostMapping("/ads")
    Ad newAd(@RequestBody Ad newAd) {
        adService.createAd(newAd);
        log.info("New Ad Created: " + newAd);
        return newAd;
    }

    /**
     * Gets all created advertisements on the home page.
     *
     * @return list of all created advertisements
     */
    @GetMapping("/ads")
    List<Ad> getAllAds() {
        List<Ad> allAds = adService.loadAllAds();
        log.debug("All Current Ads: " + allAds);
        return allAds;
    }

    /**
     * Removes the selected advertisement from the database
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return the Ad successfully removed from the database
     */
    @DeleteMapping("/ads/{id}")
    public Ad deleteAd(@PathVariable("id") Integer id) {
        Ad deletedAd = adService.deleteAd(id);
        log.info("Ad Deleted: " + deletedAd);
        return deletedAd;
    }

    /**
     * Gets all advertisements saved by the current user.
     *
     * @return list of all saved advertisements
     */
    @GetMapping("/saved_ads")
    List<Ad> getSavedAds() {
        List<Ad> savedAds = adService.loadSavedAds();
        log.debug("List of Saved Ads: " + savedAds);
        return savedAds;
    }

    /**
     * Saves the advertisement for the current user. Changes the advertisement status from "unsaved" to "saved".
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return the Ad successfully changed to "save" in the database
     */
    @PutMapping("/saved_ads/{id}")
    Ad changeAdStatus(@PathVariable("id") Integer id) {
        Ad savedAd = adService.saveAd(id);
        log.info("Ad # " + savedAd.getId() + "saved");
        log.debug("Ad # " + savedAd.getId() + "saved: " + savedAd);
        return savedAd;
    }

    /**
     * Removes the selected advertisement from the list of saved ads for the current user.
     * @param id the ID number of the selected advertisement, as given by the database
     * @return the Ad successfully changed to "unsaved" in the database
     */
    @DeleteMapping("/saved_ads/{id}")
    Ad removedSavedAd(@PathVariable("id") Integer id) {
        Ad unsavedAd = adService.removeSavedAd(id);
        log.info("Ad # " + unsavedAd.getId() + "unsaved");
        log.debug("Ad # " + unsavedAd.getId() + "unsaved: " + unsavedAd);
        return unsavedAd;
    }
}