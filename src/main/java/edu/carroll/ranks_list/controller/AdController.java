package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.AdForm;
import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.service.AdService;
import edu.carroll.ranks_list.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    private final UserService userService;

    /**
     * Constructor for the Ad Controller. Creates a service for the advertisements business logic.
     *
     * @param adService contains all logic related to advertisement data
     */
    public AdController(AdService adService, UserService userService) {
        this.adService = adService;
        this.userService = userService;
    }

    /**
     * Gets all created advertisements for display on the home page.
     *
     * @return list of all advertisements in the database
     */
    @GetMapping("/ads")
    public List<Ad> getAllAds() {
        List<Ad> allAds = adService.loadAllAds();
        log.debug("Number of Ads: " + allAds.size());
        return allAds;
    }

    /**
     * Returns the Ad object with the designated ID
     *
     * @param id the ID number of the desired advertisement
     * @return Ad object with the designated ID number
     */
    @GetMapping("/ads/{id}")
    public Ad getAd(@PathVariable("id") Integer id) {
        return adService.getReferenceById(id);
    }

    /**
     * Gets all advertisements starred by the current user.
     *
     * @return list of all user's starred advertisements
     */
    @GetMapping("/starred_ads")
    public List<Ad> getStarredAds() {
        List<Ad> starredAds = adService.loadStarredAds();
        log.debug("Number of Starred Ads: " + starredAds.size());
        return starredAds;
    }

    /**
     * Gets all advertisements created by the current user.
     *
     * @return list of all advertisements created by the current user
     */
    @GetMapping("/my_ads")
    public List<Ad> getCreatedAds(HttpServletRequest request) {
        // Get the ID number of the current user from the list of cookies
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));
        log.debug("Current UserID: " + currentUserId);

        List<Ad> createdAds = adService.loadCreatedAds(currentUserId);
        log.debug("Number of Ads Created by User #" + currentUserId + ": " + createdAds.size());
        return createdAds;
    }

    /**
     * Creates a new advertisement and adds it to the database.
     *
     * @param adForm  Contains the data to used for the created ad
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return the Ad successfully added to the database
     */
    @PostMapping("/ads")
    public boolean newAd(@RequestBody AdForm adForm, HttpServletRequest request) {
        HttpSession session = request.getSession();
        log.info("Session id", session.getAttribute("userID"));
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));
        User currentUser = userService.getReferenceById(currentUserId);

        return adService.createAd(adForm.getName(), adForm.getDescription(), adForm.getPrice(), currentUser);
    }

    /**
     * Edits an advertisement using the information provided.
     *
     * @param id      the ID of the advertisement to be edited
     * @param adForm  the new data to be connected with the advertisement
     * @return true if the designated ad is successfully edited; false otherwise
     */
    @PutMapping("/edit_ad/{id}")
    public boolean editAd(@PathVariable("id") Integer id, @RequestBody AdForm adForm) {
        return adService.editAd(adForm.getName(), adForm.getDescription(), adForm.getPrice(), id);
    }

    /**
     * Stars the advertisement for the current user. Changes the advertisement status from "unstarred" to "starred".
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return true if the advertisement with the designated ID is successfully starred
     */
    @PutMapping("/starred_ads/{id}")
    public boolean changeAdStatus(@PathVariable("id") Integer id) {
        return adService.starAd(id);
    }

    /**
     * Removes the selected advertisement from the database
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return true if the advertisement with the designated ID is successfully deleted; false otherwise
     */
    @DeleteMapping("/my_ads/{id}")
    public boolean deleteAd(@PathVariable("id") Integer id) {
        return adService.deleteAd(id);
    }

    /**
     * Removes the selected advertisement from the list of starred ads for the current user.
     *
     * @param id the ID number of the selected advertisement, as given by the database
     * @return true if the advertisement with the designated ID is successfully unstarred; false otherwise
     */
    @DeleteMapping("/starred_ads/{id}")
    public boolean removedStarredAd(@PathVariable("id") Integer id) {
        return adService.removeStarredAd(id);
    }
}