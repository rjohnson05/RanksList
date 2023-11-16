package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.form.AdForm;
import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import edu.carroll.ranks_list.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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

    private final AdService adService;
    private final UserService userService;
    private final StarService starService;

    /**
     * Constructor for the Ad Controller. Creates a service for the advertisements business logic.
     *
     * @param adService   contains all logic related to advertisement data
     * @param userService contains all logic related to user data
     * @param starService contains all logic related to star data
     */
    public AdController(AdService adService, UserService userService, StarService starService) {
        this.adService = adService;
        this.userService = userService;
        this.starService = starService;
    }

    /**
     * Gets all created advertisements for display on the home page.
     *
     * @return list of all advertisements in the database
     */
    @GetMapping("/ads")
    public List<Ad> getAllAds() {
        return adService.loadAllAds();
    }

    /**
     * Returns the Ad object with the designated ID.
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
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return list of all of a user's starred advertisements
     */
    @GetMapping("/starred_ads")
    public List<Ad> getStarredAds(HttpServletRequest request) {
        // Get the ID number of the current user from the session
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));

        return starService.loadStarredAds(currentUserId);
    }

    /**
     * Gets all advertisements created by the current user.
     *
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return list of all advertisements created by the current user
     */
    @GetMapping("/my_ads")
    public List<Ad> getCreatedAds(HttpServletRequest request) {
        // Get the ID number of the current user from the session
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));

        return adService.loadCreatedAds(currentUserId);
    }

    /**
     * Determines if the designated ad is starred for the current user.
     *
     * @param adId    Integer representing the ID of the desired advertisement
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return true if the designated ad is starred; false otherwise
     */
    @GetMapping("/ad_starred/{id}")
    public boolean isAdStarred(@PathVariable("id") Integer adId, HttpServletRequest request) {
        // Get the ID number of the current user from the session
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));

        return starService.isAdStarred(adId, currentUserId);
    }

    /**
     * Creates a new advertisement and adds it to the database.
     *
     * @param adForm  contains the data to be used for the created ad
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return true if the ad is successfully added to the database; false otherwise
     */
    @PostMapping("/ads")
    public boolean newAd(@RequestBody AdForm adForm, HttpServletRequest request) {
        // Get the ID number of the current user from the session
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));
        User currentUser = userService.getReferenceById(currentUserId);

        return adService.createAd(adForm.getName(), adForm.getDescription(), adForm.getPrice(), currentUser);
    }

    /**
     * Edits an advertisement to the provided information.
     *
     * @param id      the ID of the advertisement to be edited
     * @param adForm  the new data to be used for the advertisement
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return true if the designated ad is successfully edited; false otherwise
     */
    @PutMapping("/edit_ad/{id}")
    public boolean editAd(@PathVariable("id") Integer id, @RequestBody AdForm adForm, HttpServletRequest request) {
        // Get the ID number of the current user from the session
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));
        User currentUser = userService.getReferenceById(currentUserId);

        return adService.editAd(adForm.getName(), adForm.getDescription(), adForm.getPrice(), id, currentUser);
    }

    /**
     * Toggles the status of whether an advertisement is starred. If the starred status for the current user for an
     * advertisement is currently "true", it will be changed to "false", and vice versa.
     *
     * @param id      the ID number of the selected advertisement, as given by the database
     * @param request HttpServletRequest object that allows access to parameters of an HTTP request
     * @return true if the advertisement with the designated ID is successfully starred
     */
    @PutMapping("/starred_ads/{id}")
    public boolean changeAdStatus(@PathVariable("id") Integer id, HttpServletRequest request) {
        // Get the ID number of the current user from the session
        HttpSession session = request.getSession();
        Integer currentUserId = Integer.parseInt((String) session.getAttribute("userID"));

        return starService.changeStarStatus(id, currentUserId);
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
}