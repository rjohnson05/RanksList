package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.Star;
import edu.carroll.ranks_list.repository.AdRepository;
import edu.carroll.ranks_list.repository.StarRepository;
import edu.carroll.ranks_list.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for stars. Contains all business logic for dealing with stars.
 *
 * @author Ryan Johnson, Hank Rugg
 */
@Service
public class StarServiceImpl implements StarService {

    private static final Logger log = LoggerFactory.getLogger(StarServiceImpl.class);
    private final StarRepository starRepo;
    private final AdRepository adRepo;
    private final UserRepository userRepo;

    /**
     * Constructor for the Star Service, creating the service with a Star Repo.
     *
     * @param starRepo Repository for stars
     * @param adRepo   Repository for advertisement
     * @param userRepo Repository for users
     */
    public StarServiceImpl(StarRepository starRepo, AdRepository adRepo, UserRepository userRepo) {
        this.starRepo = starRepo;
        this.adRepo = adRepo;
        this.userRepo = userRepo;
    }

    /**
     * Determines if a designated advertisement is starred by a given user.
     *
     * @param adId   int representing the ID of the advertisement to be checked
     * @param userId int representing the ID of the current user
     * @return true if the ad is starred by the current user; false otherwise
     */
    public boolean isAdStarred(int adId, int userId) {
        // Only checks the starred status if an ad with the designated id exists
        if (!adRepo.existsById(adId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        // Only checks the starred status if a user with the designated id exists
        if (!userRepo.existsById(userId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        List<Star> adStars = starRepo.getReferenceByAd(adRepo.getReferenceById(adId));
        List<Star> userStars = starRepo.getReferenceByUser(userRepo.getReferenceById(userId));
        userStars.retainAll(adStars);
        if (userStars.isEmpty()) {
            log.debug("Star has not been created for Ad #" + adId + " and User #" + userId + " combination");
            return false;
        }
        log.debug("Star for Ad #" + adId + " and User#" + userId + " has status of " + userStars.get(0).getStarred());
        return userStars.get(0).getStarred();
    }

    /**
     * Toggles the status of whether a star is starred.
     *
     * @param adId   int representing the ID of the ad being starred/unstarred
     * @param userId int representing the ID of the current user starring/unstarring the advertisement
     * @return true if the starred status for the specified ad is successfully changed; false otherwise
     */
    public boolean changeStarStatus(int adId, int userId) {
        // Only checks the starred status if an ad with the designated id exists
        if (!adRepo.existsById(adId)) {
            log.debug("Unsuccessful attempt to star ad due to invalid ID");
            return false;
        }
        // Only checks the starred status if a user with the designated id exists
        if (!userRepo.existsById(userId)) {
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
                starRepo.save(userStars.get(0));
                log.debug("Star Unstarred for Ad #" + adId + " and User #" + userId);
                return false;
            }
            userStars.get(0).setStarred(true);
            starRepo.save(userStars.get(0));
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
     * @param userId int representing the ID of the current user starring/unstarring the advertisement
     * @return List of all Ad objects with a "starred" status of True
     */
    public List<Ad> loadStarredAds(int userId) {
        // Checks the starred status if a user with the designated id exists
        if (!userRepo.existsById(userId)) {
            log.debug("Unsuccessful attempt to unstar ad due to invalid ID");
            return null;
        }
        List<Star> userStars = starRepo.getReferenceByUser(userRepo.getReferenceById(userId));
        log.debug("userStars: " + userStars);
        List<Ad> starredAds = new ArrayList<>();
        for (Star star : userStars) {
            if (star.getStarred()) {
                starredAds.add(star.getAd());
                log.debug("starredAds: " + starredAds);
            }
        }
        log.debug("Loading all Starred Ads: " + starredAds.size() + " ad(s) loaded");
        return starredAds;
    }
}
