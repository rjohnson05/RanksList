package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class StarServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(StarServiceImpl.class);

    private final String name = "testname";
    private final Float price = 5.0F;
    private final String description = "testdescription";

    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @Autowired
    private StarService starService;

    @BeforeEach
    public void beforeTest() {
        userService.createUser("Username1", "Password@1");
        adService.createAd(name, description, price, userService.findByUsernameIgnoreCase("Username1").get(0).getId());
    }

    // Ensures isAdStarred() returns false when an ad is not starred for a given user
    @Test
    public void isAdStarredFalse() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad = adService.loadAllAds().get(0);
        assertFalse("isAdStarredFalse: should return false when an ad isn't starred", starService.isAdStarred(ad.getId(), user.getId()));
    }

    // Ensures isAdStarred() returns true when an ad is starred for a given user
    @Test
    public void isAdStarredTrue() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad = adService.loadAllAds().get(0);
        assertTrue("isAdStarredTrue: should return true when starring unstarred ad", starService.changeStarStatus(ad.getId(), user.getId()));
        assertTrue("isAdStarredTrue: should return true when an ad is starred", starService.isAdStarred(ad.getId(), user.getId()));
    }

    // Ensures isAdStarred() returns the correct booleans for several stars
    @Test
    public void isAdStarredMultipleAds() {
        User user1 = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad1 = adService.loadAllAds().get(0);
        assertTrue("isAdStarredMultipleAds: user should be created successfully", userService.createUser("Username2", "Password@1"));
        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        assertTrue("isAdStarredMultipleAds: should successfully create ad", adService.createAd(name + "1", description, price, user2.getId()));

        List<Ad> createdAds = adService.loadCreatedAds(user2.getId());
        assertEquals("isAdStarredMultipleAds: loadCreatedAds() should only return 1 ad", 1, createdAds.size());
        Ad ad2 = createdAds.get(0);
        assertTrue("isAdStarredMultipleAds: should successfully star unstarred ad", starService.changeStarStatus(ad2.getId(), user1.getId()));

        assertTrue("isAdStarredMultipleAds: should return true for starred ad", starService.isAdStarred(ad2.getId(), user1.getId()));
        assertFalse("isAdStarredMultipleAds: should return false for unstarred ad", starService.isAdStarred(ad1.getId(), user1.getId()));
        assertFalse("isAdStarredMultipleAds: should return false for unstarred ad", starService.isAdStarred(ad1.getId(), user2.getId()));
        assertFalse("isAdStarredMultipleAds: should return false for unstarred ad", starService.isAdStarred(ad2.getId(), user2.getId()));
    }

    // Ensures isAdStarred() returns false when the supplied ad ID is invalid
    @Test
    public void isAdStarredInvalidAd() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        assertFalse("isAdStarredInvalidAd: should return false when an passed invalid ad", starService.isAdStarred(0, user.getId()));
    }

    // Ensures isAdStarred() returns false when the supplied user ID is invalid
    @Test
    public void isAdStarredInvalidUser() {
        Ad ad = adService.loadAllAds().get(0);
        assertFalse("isAdStarredInvalidUser: should return false when an passed invalid user", starService.isAdStarred(ad.getId(), 0));
    }

    // Ensures isAdStarred() returns false when both the supplied ad ID and user ID are invalid
    @Test
    public void isAdStarredInvalidAdUser() {
        assertFalse("isAdStarredInvalidAdUser: should return false when an passed invalid ad and user", starService.isAdStarred(0, 0));
    }

    // Ensures changeStarStatus() changes star status to true if originally false
    @Test
    public void changeStarStatusUnstarredAd() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad = adService.loadAllAds().get(0);
        assertTrue("changeStarStatusUnstarredAd: should return true with an unstarred ad", starService.changeStarStatus(ad.getId(), user.getId()));
        assertTrue("changeStarStatusUnstarredAd: ad should now be starred", starService.isAdStarred(ad.getId(), user.getId()));
    }

    // Ensures changeStarStatus() changes star status to false if originally true
    @Test
    public void changeStarStatusStarredAd() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad = adService.loadAllAds().get(0);
        assertTrue("changeStarStatusStarredAd: should star ad successfully", starService.changeStarStatus(ad.getId(), user.getId()));
        assertFalse("changeStarStatusStarredAd: should return false with a starred ad", starService.changeStarStatus(ad.getId(), user.getId()));
        assertFalse("changeStarStatusStarredAd: ad should not be starred", starService.isAdStarred(ad.getId(), user.getId()));
    }

    // Ensures changeStarStatus() changes star status to true if originally false with multiple ads
    @Test
    public void changeStarStatusMultipleAds() {
        User user1 = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad1 = adService.loadAllAds().get(0);
        assertTrue("changeStarStatusMultipleAds: should successfully create user", userService.createUser("Username2", "Password@1"));
        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        assertTrue("changeStarStatusMultipleAds: should successfully create ad", adService.createAd(name + "1", description, price, user2.getId()));
        List<Ad> createdAd = adService.loadCreatedAds(user2.getId());
        assertEquals("changeStarStatusMultipleAds: size of loadCreatedAds() should be 1", 1, createdAd.size());
        Ad ad2 = createdAd.get(0);

        assertTrue("changeStarStatusMultipleAds: should return true with an unstarred ad", starService.changeStarStatus(ad1.getId(), user1.getId()));
        assertTrue("changeStarStatusMultipleAds: ad should now be starred", starService.isAdStarred(ad1.getId(), user1.getId()));
        assertFalse("changeStatStatusMultipleAds: other ads for other users should be unstarred still", starService.isAdStarred(ad1.getId(), user2.getId()));
        assertFalse("changeStatStatusMultipleAds: other ads for other users should be unstarred still", starService.isAdStarred(ad2.getId(), user1.getId()));
        assertFalse("changeStatStatusMultipleAds: other ads for other users should be unstarred still", starService.isAdStarred(ad2.getId(), user1.getId()));
    }

    // Ensures changeStarStatus() returns false when the supplied ad ID is invalid
    @Test
    public void changeStarStatusInvalidAd() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        assertFalse("changeStarStatusInvalidAd: should return false when given invalid ad ID", starService.changeStarStatus(0, user.getId()));
    }

    // Ensures changeStarStatus() returns false when the supplied user ID is invalid
    @Test
    public void changeStarStatusInvalidUser() {
        Ad ad = adService.loadAllAds().get(0);
        assertFalse("changeStarStatusInvalidUser: should return false when given invalid user ID", starService.changeStarStatus(ad.getId(), 0));
    }

    // Ensures changeStarStatus() returns false when both the supplied ad ID and user ID are invalid
    @Test
    public void changeStarStatusInvalidAdUser() {
        assertFalse("changeStarStatusInvalidAdUser: should return false when given invalid ad ID and user ID", starService.changeStarStatus(0, 0));
    }

    // Ensures loadStarredAds() returns empty list if user has no starred ads
    @Test
    public void loadStarredAdsNoStarred() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        assertEquals("loadStarredAdsNoStarred: should return 0 when no starred ads present", 0, starService.loadStarredAds(user.getId()).size());
    }

    // Ensures loadStarredAds() returns length of 1 if user has a single starred ad
    @Test
    public void loadStarredAdsOneStarred() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad = adService.loadAllAds().get(0);
        assertTrue("loadStarredAdsOneStarred: should successfully star ad", starService.changeStarStatus(ad.getId(), user.getId()));
        List<Ad> starredAds = starService.loadStarredAds(user.getId());
        assertEquals("loadStarredAdsOneStarred: should return 1 when one starred ad is present", 1, starredAds.size());
        assertEquals("loadStarredAdsOneStarred: name of loaded ad should be same as original ad", name, starredAds.get(0).getName());
        assertEquals("loadStarredAdsOneStarred: price of loaded ad should be same as original ad", price, starredAds.get(0).getPrice());
        assertEquals("loadStarredAdsOneStarred: description of loaded ad should be same as original ad", description, starredAds.get(0).getDescription());
        assertEquals("loadStarredAdsOneStarred: user of loaded ad should be same as original ad", user, starredAds.get(0).getUser());
    }

    // Ensures loadStarredAds() returns length of 2 if user has 2 starred ads
    @Test
    public void loadStarredAdsTwoStarred() {
        User user1 = userService.findByUsernameIgnoreCase("Username1").get(0);
        Ad ad1 = adService.loadAllAds().get(0);
        userService.createUser("Username2", "Password@1");
        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        assertTrue("loadStarredAdsTwoStarred: should successfully create ad", adService.createAd(name + "1", description, price, user2.getId()));
        List<Ad> createdAd = adService.loadCreatedAds(user2.getId());
        assertEquals("loadStarredAdsTwoStarred: size of loadCreatedAds() should be 1", 1, adService.loadCreatedAds(user2.getId()).size());
        Ad ad2 = createdAd.get(0);

        assertTrue("loadStarredAdsTwoStarred: should successfully star ad", starService.changeStarStatus(ad1.getId(), user1.getId()));
        assertTrue("loadStarredAdsTwoStarred: should successfully star ad", starService.changeStarStatus(ad2.getId(), user2.getId()));

        List<Ad> starredAds = starService.loadStarredAds(user1.getId());
        assertEquals("loadStarredAdsTwoStarred: should return 1 when one starred ad for this user is present", 1, starredAds.size());
        assertEquals("loadStarredAdsTwoStarred: name of loaded ad should be same as original ad", name, starredAds.get(0).getName());
        assertEquals("loadStarredAdsTwoStarred: price of loaded ad should be same as original ad", price, starredAds.get(0).getPrice());
        assertEquals("loadStarredAdsTwoStarred: description of loaded ad should be same as original ad", description, starredAds.get(0).getDescription());
        assertEquals("loadStarredAdsTwoStarred: user of loaded ad should be same as original ad", user1, starredAds.get(0).getUser());

        starredAds = starService.loadStarredAds(user2.getId());
        assertEquals("loadStarredAdsOneStarred: should return 1 when one starred ad for this user is present", 1, starredAds.size());
        assertEquals("loadStarredAdsOneStarred: name of loaded ad should be same as original ad", name + "1", starredAds.get(0).getName());
        assertEquals("loadStarredAdsOneStarred: price of loaded ad should be same as original ad", price, starredAds.get(0).getPrice());
        assertEquals("loadStarredAdsOneStarred: description of loaded ad should be same as original ad", description, starredAds.get(0).getDescription());
        assertEquals("loadStarredAdsOneStarred: user of loaded ad should be same as original ad", user2, starredAds.get(0).getUser());
    }

    // Ensures loadStarredAds() returns false if the supplied user ID is invalid
    @Test
    public void loadStarredAdsInvalidUser() {
        assertEquals("loadStarredAdsInvalidUser: should return empty list if given invalid user ID", 0, starService.loadStarredAds(0).size());
    }
}
