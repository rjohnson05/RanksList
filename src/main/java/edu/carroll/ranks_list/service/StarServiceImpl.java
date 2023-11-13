package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Star;
import edu.carroll.ranks_list.repository.AdRepository;
import edu.carroll.ranks_list.repository.StarRepository;
import edu.carroll.ranks_list.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for stars. Contains all business logic for dealing with stars.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@Service
public class StarServiceImpl implements StarService {

    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    private final StarRepository starRepo;
    private final AdRepository adRepo;
    private final UserRepository userRepo;

    /**
     * Constructor for the Star Service, creating the service with a Star Repo.
     *
     * @param starRepo Repository for stars
     */
    public StarServiceImpl(StarRepository starRepo, AdRepository adRepo, UserRepository userRepo) {
        this.starRepo = starRepo;
        this.adRepo = adRepo;
        this.userRepo = userRepo;
    }

    public boolean isAdStarred(Integer adId, Integer userId) {
        // Only checks the starred status if an ad with the designated id exists
        if (adId == null || !adRepo.existsById(adId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        // Only checks the starred status if a user with the designated id exists
        if (userId == null || !userRepo.existsById(userId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        List<Star> adStars = starRepo.getReferenceByAd(adRepo.getReferenceById(adId));
        List<Star> userStars = starRepo.getReferenceByUser(userRepo.getReferenceById(userId));
        userStars.retainAll(adStars);
        if (userStars.isEmpty()) {
            log.debug("Star has not been created for Ad #" + adId + " and User #" + userId + "combination");
            return false;
        }
        log.debug("Star for Ad #" + adId + " and User#" + userId + " has status of " + userStars.get(0).getStarred());
        return userStars.get(0).getStarred();
    }

    /**
     * Places a designated advertisement onto the list of starred advertisements.
     *
     * @param adId Integer representing the ID of the ad being starred
     * @param userId Integer representing the ID of the current user starring the advertisement
     *
     * @return true if the ad is successfully starred; false otherwise
     */
    public boolean changeStarStatus(Integer adId, Integer userId) {
        log.debug("Enters starring method");
        // Only checks the starred status if an ad with the designated id exists
        if (adId == null || !adRepo.existsById(adId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        // Only checks the starred status if a user with the designated id exists
        if (userId == null || !userRepo.existsById(userId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        List<Star> adStars = starRepo.getReferenceByAd(adRepo.getReferenceById(adId));
        List<Star> userStars = starRepo.getReferenceByUser(userRepo.getReferenceById(userId));
        userStars.retainAll(adStars);
        // If the star has already been created, it may need to restarred or left as starred
        if (!userStars.isEmpty()) {
            if (userStars.size() != 1) {
                log.debug("Unsuccessful attempt to star ad: Returned more than one stars for the current ad/user combination");
                return false;
            }
            if (userStars.get(0).getStarred()) {
                userStars.get(0).setStarred(false);
                log.debug("Star Unstarred for Ad #" + adId + " and User #" + userId);
                return true;
            }
            userStars.get(0).setStarred(true);
            log.debug("Star Starred for Ad #" + adId + " and User #" + userId);
            return true;
        }
        Star star = new Star(adRepo.getReferenceById(adId), userRepo.getReferenceById(userId));
        starRepo.save(star);
        log.debug("Star Created and Starred for Ad #" + adId + " and User #" + userId);
        return true;
    }

    /**
     * Returns a list of all starred advertisements.
     *
     * @return List of all Ad objects with a "starred" status of True
     */
    public List<Ad> loadStarredAds(Integer userId) {
        // Checks the starred status if a user with the designated id exists
        if (userId == null || !userRepo.existsById(userId)) {
            log.debug("Unsuccessful attempt to unstar ad due to invalid ID");
            return null;
        }
        List<Star> userStars = starRepo.getReferenceByUser(userRepo.getReferenceById(userId));
        List<Ad> starredAds = null;
        for (Star star : userStars) {
            if (star.getStarred()) {
                starredAds.add(star.getAd());
            }
        }
        log.debug("Loading all Starred Ads: " + starredAds.size() + " ad(s) loaded");
        return starredAds;
    }
}
