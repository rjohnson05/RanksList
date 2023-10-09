package edu.carroll.ranks_list.controller;


import edu.carroll.ranks_list.form.AdForm;
import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.service.AdService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

/**
 * API for HTTP requests regarding advertisements. Specifies how to handle HTTP requests that involve advertisement data.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
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
     * @param adForm Ad to be added to the database
     * @return the Ad successfully added to the database
     */
    @PostMapping("/ads")
    boolean newAd(@RequestBody AdForm adForm, HttpServletRequest request) {
        Integer currentUser = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("userID")) {
                currentUser = Integer.parseInt(c.getValue());
                break;
            }
        }

        return adService.createAd(adForm.getName(), adForm.getDescription(), adForm.getPrice(), currentUser);
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
     * Gets all advertisements starred by the current user.
     *
     * @return list of all starred advertisements
     */
    @GetMapping("/starred_ads")
    List<Ad> getStarredAds() {
        List<Ad> starredAds = adService.loadStarredAds();
        log.debug("List of Starred Ads: " + starredAds);
        return starredAds;
    }

    /**
     * Saves the advertisement for the current user. Changes the advertisement status from "unstarred" to "starred".
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return the Ad successfully changed to "save" in the database
     */
    @PutMapping("/starred_ads/{id}")
    Ad changeAdStatus(@PathVariable("id") Integer id) {
        Ad starredAd = adService.starAd(id);
        log.info("Ad # " + starredAd.getId() + "starred");
        log.debug("Ad # " + starredAd.getId() + "starred: " + starredAd);
        return starredAd;
    }

    /**
     * Removes the selected advertisement from the list of starred ads for the current user.
     * @param id the ID number of the selected advertisement, as given by the database
     * @return the Ad successfully changed to "unstarred" in the database
     */
    @DeleteMapping("/starred_ads/{id}")
    Ad removedStarredAd(@PathVariable("id") Integer id) {
        Ad unstarredAd = adService.removeStarredAd(id);
        log.info("Ad # " + unstarredAd.getId() + "unstarred");
        log.debug("Ad # " + unstarredAd.getId() + "unstarred: " + unstarredAd);
        return unstarredAd;
    }

    /**
     * Gets all advertisements created by the current user.
     *
     * @return list of all advertisements created by the current user
     */
    @GetMapping("/my_ads")
    List<Ad> createdAds(HttpServletRequest request) {
        // Get the ID number of the current user from the list of cookies
        Integer currentUser = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie c : cookies) {
            if (c.getName().equals("userID")) {
                currentUser = Integer.parseInt(c.getValue());
                break;
            }
        }

        List<Ad> createdAds = adService.loadCreatedAds(currentUser);
        log.debug("List of Starred Ads: " + createdAds);
        return createdAds;
    }

    /**
     * Removes the selected advertisement from the database
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return the Ad successfully removed from the database
     */
    @DeleteMapping("/my_ads/{id}")
    public Ad deleteMyAd(@PathVariable("id") Integer id) {
        Ad deletedAd = adService.deleteAd(id);
        log.info("Ad Deleted: " + deletedAd);
        return deletedAd;
    }
}