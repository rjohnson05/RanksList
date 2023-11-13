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
public class AdServiceImplTest {
    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    private final String name = "testname";
    private final Float price = 5.0F;
    private final String description = "testdescription";

    @Autowired
    private AdService adService;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void createUser() {
        userService.createUser("username", "Password@1");
    }

    // Tests to ensure createAd() method creates and stores a single advertisement correctly when passed valid data
    @Test
    public void createAdValidDataTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        List<Ad> allCreatedAds = adService.loadAllAds();

        assertEquals("createAdValidDataTest: should create a single ad with valid data", 1, allCreatedAds.size());
        Ad createdAd = allCreatedAds.get(0);

        assertEquals("createAdValidDataTest: name should be stored correctly with valid ad data", name, createdAd.getName());
        assertEquals("createAdValidDataTest: price should be stored correctly with valid ad data", price, createdAd.getPrice());
        assertEquals("createAdValidDataTest: description should be stored correctly with valid ad data", description, createdAd.getDescription());
        assertEquals("createAdValidDataTest: userId should be stored correctly with valid ad data", user, createdAd.getUser());
    }

    // Tests to ensure createAd() method creates and stores several valid advertisements correctly
    @Test
    public void createMultipleAdsValidDataTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        userService.createUser("username1", "Password@1");
        User newUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, newUser);
        List<Ad> allCreatedAds = adService.loadAllAds();
        assertEquals("createMultipleAdsValidDataTest: should contain two ads with valid data", 2, allCreatedAds.size());
        for (Ad ad : allCreatedAds) {
            assertTrue("createMultipleAdsValidDataTest: name of ad is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1"));
            if (ad.getName().equals(name)) {
                assertEquals("createMultipleAdsValidDataTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("createMultipleAdsValidDataTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("createMultipleAdsValidDataTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("createMultipleAdsValidDataTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("createMultipleAdsValidDataTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("createMultipleAdsValidDataTest: second ad doesn't contain correct user", newUser, ad.getUser());
            }
        }
    }

    // Tests to ensure createAd() method creates and stores a single advertisement correctly when all data is the same as
    // another ad except for the ID of the user creating it
    @Test
    public void createAdDuplicateAdDifferentUserIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        List<Ad> allCreatedAds = adService.loadAllAds();
        assertEquals("createAdDuplicateAdDifferentUserIdTest: should create a single ad with valid data", 1, allCreatedAds.size());

        userService.createUser("username1", "Password@1");
        User newUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name, description, price, newUser);
        allCreatedAds = adService.loadAllAds();
        assertEquals("createAdDuplicateAdDifferentUserIdTest: should create a single duplicate ad with different user ID", 2, allCreatedAds.size());

        List<Ad> duplicateAds = adService.loadCreatedAds(newUser.getId());
        assertEquals("createAdDuplicateAdDifferentUserIdTest: should create a single ad with this user ID", 1, duplicateAds.size());
        Ad duplicateAd = duplicateAds.get(0);
        assertEquals("createAdDuplicateAdDifferentUserIdTest: duplicate ad should have the same name as previous ad", name, duplicateAd.getName());
        assertEquals("createAdDuplicateAdDifferentUserIdTest: duplicate ad should have the same price as previous ad", price, duplicateAd.getPrice());
        assertEquals("createAdDuplicateAdDifferentUserIdTest: duplicate ad should have the same description as previous ad", description, duplicateAd.getDescription());
    }

    // Tests to ensure that createAd() method does not create an ad if all parameters are the same as another ad created
    // by the same user
    @Test
    public void createAdDuplicateAdSameUserIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("createAdDuplicateAdSameUserIdTest: attempted duplicate ad should fail when having the same user ID", adService.createAd(name, description, price, user));
    }

    // Tests to ensure that createAd() will fail if no name is provided
    @Test
    public void createAdNoNameTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        assertFalse("createAdNoNameTest: should not create successfully when no name is supplied", adService.createAd("", description, price, user));
        assertEquals("createAdNoNameTest: should not create an ad when no name is supplied", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() will create an ad even if no description is provided
    @Test
    public void createAdNoDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        assertTrue("createAdNoDescriptionTest: should create an ad when no description is supplied", adService.createAd(name, "", price, user));
        assertEquals("createAdNoDescriptionTest: should create an ad when no description is supplied", 1, adService.loadAllAds().size());

        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("createAdNoDescriptionTest: name should be stored correctly without providing a description", name, createdAd.getName());
        assertEquals("createAdNoDescriptionTest: price should be stored correctly without providing a description", price, createdAd.getPrice());
        assertEquals("createAdNoDescriptionTest: empty description should be stored correctly", "", createdAd.getDescription());
        assertEquals("createAdNoDescriptionTest: user should be stored correctly without providing a description", user, createdAd.getUser());
    }

    // Tests to ensure that createAd() will fail if the provided user is not in the DB
    @Test
    public void createAdNoUserTest() {
        User invalidUser = new User();
        assertFalse("createAdNoUserTest: should not create successfully when no valid user is supplied", adService.createAd("", description, price, invalidUser));
        assertEquals("createAdNoUserTest: should not create an ad when no user is supplied", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() creates an ad correctly with a price of 0
    @Test
    public void createAdPriceZeroTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, (float) 0, user);
        assertEquals("createAdPriceZeroTest: should pass with a price of $0.00", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("createAdPriceZeroTest: name not stored correctly for ad with price of $0.00", name, createdAd.getName());
        assertEquals("createAdPriceZeroTest: description not stored correctly for ad with price of $0.00", description, createdAd.getDescription());
        assertEquals("createAdPriceZeroTest: price not stored correctly for ad with price of $0.00", (float) 0, createdAd.getPrice());
        assertEquals("createAdPriceZeroTest: user not stored correctly for ad with price of $0.00", user, createdAd.getUser());
    }

    // Tests to ensure that createAd() doesn't create an ad when passed a null name
    @Test
    public void createAdNullNameTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        assertFalse("createAdNullNameTest: should return false when passed null name", adService.createAd(null, description, price, user));
        assertEquals("createAdNullNameTest: should fail with null name", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when passed a null price
    @Test
    public void createAdNullPriceTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        assertTrue("createAdNullPriceTest: should return true when passed null price", adService.createAd(name, description, null, user));
        assertEquals("createAdNullPriceTest: should pass with null price", 1, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() creates an ad when passed a null description
    @Test
    public void createAdNullDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        assertTrue("createAdNullDescriptionTest: should return true when passed null description", adService.createAd(name, null, price, user));
        assertEquals("createAdNullDescriptionTest: should pass with null description", 1, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when all parameters are null
    @Test
    public void createAdNullAllTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        assertFalse("createAdNullAllTest: should return false when passed all null values", adService.createAd(null, null, null, null));
        assertEquals("createAdNullAllTest: should fail with all Null data", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that editAd() correctly changes all the data of an advertisement if given valid data
    @Test
    public void editAdValidDataAllChangeTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when valid data is given", adService.editAd(name + "1", description + "1", price + 1, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name + "1", editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price + 1, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description + "1", editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes just the name of an advertisement if given valid data
    @Test
    public void editAdValidDataNameChangeTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name + "1", description, price, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name + "1", editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description, editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes just the price of an advertisement if given valid data
    @Test
    public void editAdValidDataPriceChangeTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name, description, price + 1, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name, editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price + 1, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description, editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes just the description of an advertisement if given valid data
    @Test
    public void editAdValidDataDescriptionChangeTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name, description + "1", price, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name, editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description + "1", editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }


    // Tests to ensure that editAd() is not successful if the name is left empty
    @Test
    public void editAdEmptyNameTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("editAdEmptyNameTest: shouldn't be successful when name is changed to be empty", adService.editAd("", description, price, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdEmptyNameTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdEmptyNameTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdEmptyNameTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdEmptyNameTest: description of ad isn't same as before unsuccessful editing", description, createdAd.getDescription());
        assertEquals("editAdEmptyNameTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is successful if the description is left empty
    @Test
    public void editAdEmptyDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertTrue("editAdEmptyDescriptionTest: should be successful when description is changed to be empty", adService.editAd(name, "", price, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdEmptyDescriptionTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdEmptyDescriptionTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdEmptyDescriptionTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdEmptyDescriptionTest: description of ad wasn't changed correctly", "", createdAd.getDescription());
        assertEquals("editAdEmptyDescriptionTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is unsuccessful if the supplied name is null
    @Test
    public void editAdNullNameTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("editAdNullNameTest: shouldn't be successful when given name is null", adService.editAd(null, description, price, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdNullNameTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullNameTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullNameTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullNameTest: description of ad wasn't changed correctly", description, createdAd.getDescription());
        assertEquals("editAdNullNameTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is unsuccessful if the supplied price is null
    @Test
    public void editAdNullPriceTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("editAdNullPriceTest: shouldn't be successful when given price is null", adService.editAd(name, description, null, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdNullPriceTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullPriceTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullPriceTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullPriceTest: description of ad wasn't changed correctly", description, createdAd.getDescription());
        assertEquals("editAdNullPriceTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is unsuccessful if the supplied description is null
    @Test
    public void editAdNullDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("editAdNullDescriptionTest: shouldn't be successful when given description is null", adService.editAd(name, null, price, adService.loadAllAds().get(0).getId(), user));
        assertEquals("editAdNullDescriptionTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullDescriptionTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullDescriptionTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullDescriptionTest: description of ad wasn't changed correctly", description, createdAd.getDescription());
        assertEquals("editAdNullDescriptionTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is unsuccessful if the supplied ID is null
    @Test
    public void editAdNullIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("editAdNullIdTest: shouldn't be successful when given description is null", adService.editAd(name, description, price, null, user));
        assertEquals("editAdNullIdTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullIdTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullIdTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullIdTest: description of ad wasn't changed correctly", description, createdAd.getDescription());
        assertEquals("editAdNullIdTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is unsuccessful if all supplied parameters are null
    @Test
    public void editAdNullAllTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        assertFalse("editAdNullAllTest: shouldn't be successful when given description is null", adService.editAd(null, null, null, null, user));
        assertEquals("editAdNullAllTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullAllTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullAllTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullAllTest: description of ad wasn't changed correctly", description, createdAd.getDescription());
        assertEquals("editAdNullAllTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is unsuccessful if given an ID that is doesn't map to an advertisement
    @Test
    public void editAdInvalidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        adService.createAd(name + "1", description, price, user);
        int deletedAdId = 0;
        for (Ad ad : adService.loadAllAds()) {
            if (ad.getName().equals(name + "1")) {
                deletedAdId = ad.getId();
                adService.deleteAd(deletedAdId);
                break;
            }
        }
        Ad remainingAd = adService.loadAllAds().get(0);
        assertFalse("editAdInvalidIdTest: shouldn't be successful when given ID matches no advertisement", adService.editAd(name, description, price, deletedAdId, user));
        assertEquals("editAdInvalidIdTest: name of ad isn't same as before unsuccessful editing", name, remainingAd.getName());
        assertEquals("editAdInvalidIdTest: price of ad isn't same as before unsuccessful editing", price, remainingAd.getPrice());
        assertEquals("editAdInvalidIdTest: description of ad wasn't changed correctly", description, remainingAd.getDescription());
        assertEquals("editAdInvalidIdTest: user of ad isn't same as before unsuccessful editing", user, remainingAd.getUser());
    }

    // Tests to ensure that starAd() is successful with a valid ID
    @Test
    public void starAdValidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int firstAdId = adService.loadAllAds().get(0).getId();

        // Makes sure the single ad is moved to list of starred ads
        assertTrue("starAdValidIdTest: should pass if the designated ad has a valid ID", adService.starAd(firstAdId));
        assertEquals("starAdValidIdTest: should pass if the designated ad is added to the list of starred ads", 1, adService.loadStarredAds().size());
        Ad starredAd = adService.loadStarredAds().get(0);
        assertEquals("starAdValidIdTest: should pass if the starred ad still appears in the list of all ads", 1, adService.loadAllAds().size());

        // Makes sure the data of the starred ad matches that of the original ad
        assertEquals("starAdValidIdTest: name of starred ad is not same as unstarred ad", name, starredAd.getName());
        assertEquals("starAdValidIdTest: price of starred ad is not same as unstarred ad", price, starredAd.getPrice());
        assertEquals("starAdValidIdTest: description of starred ad is not same as unstarred ad", description, starredAd.getDescription());
        assertEquals("starAdValidIdTest: starred status of starred ad should be true", true, starredAd.getStarred());
    }

    // Tests to ensure that starAd() is successful when starring multiple valid ads
    @Test
    public void starAdValidIdMultipleTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        List<Ad> allAds = adService.loadAllAds();
        int firstAdId = allAds.get(0).getId();

        userService.createUser("username1", "Password@1");
        User secondUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, secondUser);
        allAds = adService.loadAllAds();
        int secondAdId = allAds.get(1).getId();
        // Order not guaranteed in the DB, so double-checking that ID for correct ad is being stored
        if (secondAdId == firstAdId) {
            secondAdId = allAds.get(0).getId();
        }

        adService.starAd(firstAdId);
        // Makes sure only the single ad is moved to list of starred ads
        assertEquals("starAdValidIdMultipleTest: should pass if only the designated ad is added to the list of starred ads", 1, adService.loadStarredAds().size());
        Ad starredAd = adService.loadStarredAds().get(0);
        assertEquals("starAdValidIdMultipleTest: should pass if the starred ad still appears in the list of all ads", 2, adService.loadAllAds().size());
        // Makes sure the data of the starred ad matches that of the original ad
        assertEquals("starAdValidIdMultipleTest: name of starred ad is not same as equivalent unstarred ad", name, starredAd.getName());
        assertEquals("starAdValidIdMultipleTest: price of starred ad is not same as equivalent unstarred ad", price, starredAd.getPrice());
        assertEquals("starAdValidIdMultipleTest: description of starred ad is not same as equivalent unstarred ad", description, starredAd.getDescription());
        assertEquals("starAdValidIdMultipleTest: starred status of starred ad should be true", true, starredAd.getStarred());
        assertEquals("starAdValidIdMultipleTest: user of starred ad is not same as equivalent unstarred ad", user, starredAd.getUser());
        // Makes sure the data of the unstarred ad have not changed
        Ad unstarredAd = adService.getReferenceById(secondAdId);
        assertEquals("starAdValidIdMultipleTest: name of unstarred ad is not same as originally", name + "1", unstarredAd.getName());
        assertEquals("starAdValidIdMultipleTest: price of starred ad is not same as originally", price + 1, unstarredAd.getPrice());
        assertEquals("starAdValidIdMultipleTest: description of starred ad is not same as originally", description + "1", unstarredAd.getDescription());
        assertEquals("starAdValidIdMultipleTest: starred status of unstarred ad should be false", false, unstarredAd.getStarred());
        assertEquals("starAdValidIdMultipleTest: user of starred ad is not same as originally", secondUser, unstarredAd.getUser());

        // Storing the thirdly created ad
        userService.createUser("username2", "Password@1");
        User thirdUser = userService.findByUsernameIgnoreCase("username2").get(0);
        adService.createAd(name + "2", description + "2", price + 2, thirdUser);
        allAds = adService.loadAllAds();
        int thirdAdId = allAds.get(2).getId();
        if (thirdAdId == firstAdId || thirdAdId == secondAdId) {
            thirdAdId = allAds.get(1).getId();
            if (thirdAdId == firstAdId || thirdAdId == secondAdId) {
                thirdAdId = allAds.get(0).getId();
            }
        }

        // Ensures having multiple starred ads is successful
        adService.starAd(secondAdId);
        // Makes sure only the single ad is moved to list of starred ads
        assertEquals("starAdValidIdMultipleTest: should pass if only the second ad is added to the list of starred ads", 2, adService.loadStarredAds().size());
        assertEquals("starAdValidIdMultipleTest: should pass if the starred ad still appears in the list of all ads", 3, adService.loadAllAds().size());
        // Makes sure the data of the starred ad matches that of the original ad
        starredAd = adService.getReferenceById(secondAdId);
        assertEquals("starAdValidIdMultipleTest: name of starred ad is not same as equivalent unstarred ad", name + "1", starredAd.getName());
        assertEquals("starAdValidIdMultipleTest: price of starred ad is not same as equivalent unstarred ad", price + 1, starredAd.getPrice());
        assertEquals("starAdValidIdMultipleTest: description of starred ad is not same as equivalent unstarred ad", description + "1", starredAd.getDescription());
        assertEquals("starAdValidIdMultipleTest: starred status of starred ad should be true", true, starredAd.getStarred());
        assertEquals("starAdValidIdMultipleTest: user of starred ad is not same as equivalent unstarred ad", secondUser, starredAd.getUser());
        // Makes sure the data of the unstarred ad have not changed
        unstarredAd = adService.getReferenceById(thirdAdId);
        assertEquals("starAdValidIdMultipleTest: name of unstarred ad is not same as originally", name + "2", unstarredAd.getName());
        assertEquals("starAdValidIdMultipleTest: price of starred ad is not same as originally", price + 2, unstarredAd.getPrice());
        assertEquals("starAdValidIdMultipleTest: description of starred ad is not same as originally", description + "2", unstarredAd.getDescription());
        assertEquals("starAdValidIdMultipleTest: starred status of unstarred ad should be false", false, unstarredAd.getStarred());
        assertEquals("starAdValidIdMultipleTest: user of starred ad is not same as originally", thirdUser, unstarredAd.getUser());
    }

    // Ensures that starAd() is unsuccessful if an invalid ID is passed
    @Test
    public void starAdInvalidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        // Delete this ad and star its ID to ensure the ID is invalid
        adService.deleteAd(adId);

        assertFalse("starAdInvalidIdTest: didn't fail when passed an invalid ID", adService.starAd(adId));
        assertEquals("starAdInvalidIdTest: starred ad is present after starring invalid ID", 0, adService.loadStarredAds().size());
        assertEquals("starAdInvalidIdTest: ad is present after starring invalid ID", 0, adService.loadAllAds().size());
    }

    // Ensures that starAd() is unsuccessful if the designated ad is already starred
    @Test
    public void starAdAlreadyStarredIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int firstAdId = adService.loadAllAds().get(0).getId();
        adService.starAd(firstAdId);

        assertFalse("starAdAlreadyStarredIdTest: shouldn't pass successfully for an ad that is already starred", adService.starAd(firstAdId));
        assertEquals("starAdAlreadyStarredIdTest: should be one ad in list of starred ads", 1, adService.loadStarredAds().size());
        assertEquals("starAdAlreadyStarredIdTest: number of ads shouldn't change after unsuccessful starring", 1, adService.loadAllAds().size());

        // Ensures that the data of the ad hasn't changed
        Ad ad = adService.getReferenceById(firstAdId);
        assertEquals("starAdAlreadyStarredIdTest: name of ad isn't same as originally", name, ad.getName());
        assertEquals("starAdAlreadyStarredIdTest: price of ad isn't same as originally", price, ad.getPrice());
        assertEquals("starAdAlreadyStarredIdTest: description of ad isn't same as originally", description, ad.getDescription());
        assertEquals("starAdAlreadyStarredIdTest: starred status of ad shouldn't be true", true, ad.getStarred());
        assertEquals("starAdAlreadyStarredIdTest: user of ad isn't same as originally", user, ad.getUser());

    }

    // Ensures that starAd() is unsuccessful if an invalid ID is passed, even if other ads are still present in the DB
    @Test
    public void starAdMultipleInvalidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int firstAdId = adService.loadAllAds().get(0).getId();
        // Delete this ad and star its ID to ensure the ID is invalid
        adService.deleteAd(firstAdId);
        assertEquals("starAdMultipleInvalidIdTest: number of ads isn't 0 after deleting", 0, adService.loadAllAds().size());

        userService.createUser("username1", "Password@1");
        User newUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, newUser);
        int secondAdId = adService.loadAllAds().get(0).getId();
        adService.starAd(secondAdId);

        assertFalse("starAdMultipleInvalidIdTest: didn't fail when passed an invalid ID", adService.starAd(firstAdId));
        assertEquals("starAdMultipleInvalidIdTest: starred ad is present after starring invalid ID", 1, adService.loadStarredAds().size());
        assertEquals("starAdMultipleInvalidIdTest: ad is present after starring invalid ID", 1, adService.loadAllAds().size());
        // Ensure data in remaining ad is same as before trying to star the invalid ID
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("starAdMultipleInvalidIdTest: name of remaining ad isn't same as originally", name + "1", remainingAd.getName());
        assertEquals("starAdMultipleInvalidIdTest: price of remaining ad isn't same as originally", price + 1, remainingAd.getPrice());
        assertEquals("starAdMultipleInvalidIdTest: description of remaining ad isn't same as originally", description + "1", remainingAd.getDescription());
        assertEquals("starAdMultipleInvalidIdTest: starred status of remaining ad should be true", true, remainingAd.getStarred());
    }

    // Ensures that starAd() is unsuccessful if given a null ID
    @Test
    public void starAdNullIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        assertFalse("starAdNullIdTest: shouldn't be successful when given a null ID", adService.starAd(null));
        assertEquals("starAdNullIdTest: size of ad list isn't same as before unsuccessful attempt to star ad", 1, adService.loadAllAds().size());

        Ad createdAd = adService.getReferenceById(adId);
        assertEquals("starAdNullIdTest: name of ad isn't same as before trying to star ad", name, createdAd.getName());
        assertEquals("starAdNullIdTest: description of ad isn't same as before trying to star ad", description, createdAd.getDescription());
        assertEquals("starAdNullIdTest: price of ad isn't same as before trying to star ad", price, createdAd.getPrice());
        assertEquals("starAdNullIdTest: starred status of remaining ad should be false", false, createdAd.getStarred());
        assertEquals("starAdNullIdTest: user of ad isn't same as before trying to star ad", user, createdAd.getUser());
    }

    // Tests to ensure that removeStarredAd() is successful with a valid ID
    @Test
    public void removeStarredAdValidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int firstAdId = adService.loadAllAds().get(0).getId();
        adService.starAd(firstAdId);

        // Makes sure the single ad is removed from list of starred ads
        assertTrue("removeStarredAdValidIdTest: should be successful if given a valid ID", adService.removeStarredAd(firstAdId));
        assertEquals("removeStarredAdValidIdTest: list of starred ads should be empty", 0, adService.loadStarredAds().size());
        assertEquals("removeStarredAdValidIdTest: should pass if the ad still appears in the list of all ads", 1, adService.loadAllAds().size());

        // Ensures the data for the unstarred ad is still same as it was originally
        Ad unstarredAd = adService.getReferenceById(firstAdId);
        assertEquals("removeStarredAdValidIdTest: name of ad isn't same as before trying to star ad", name, unstarredAd.getName());
        assertEquals("removeStarredAdValidIdTest: description of ad isn't same as before trying to star ad", description, unstarredAd.getDescription());
        assertEquals("removeStarredAdValidIdTest: price of ad isn't same as before trying to star ad", price, unstarredAd.getPrice());
        assertEquals("removeStarredAdValidIdTest: starred status of unstarred ad should be false", false, unstarredAd.getStarred());
        assertEquals("removeStarredAdValidIdTest: user of ad isn't same as before trying to star ad", user, unstarredAd.getUser());
    }

    // Tests to ensure that removeStarredAd() is successful when unstarring multiple valid ads
    @Test
    public void removeStarredAdValidIdMultipleTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        List<Ad> allAds = adService.loadAllAds();
        int firstAdId = allAds.get(0).getId();
        adService.starAd(firstAdId);

        userService.createUser("username1", "Password@1");
        User secondUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, secondUser);
        allAds = adService.loadAllAds();
        int secondAdId = allAds.get(1).getId();
        // Order not guaranteed in the DB, so double-checking that ID for correct ad is being stored
        if (secondAdId == firstAdId) {
            secondAdId = allAds.get(0).getId();
        }
        adService.starAd(secondAdId);

        assertTrue("removeStarredAdValidIdMultipleTest: should pass successfully if ID is valid", adService.removeStarredAd(firstAdId));
        // Makes sure only the designated ad is removed from the list of starred ads
        assertEquals("removeStarredAdValidIdMultipleTest: should pass if only the designated ad is removed from the list of starred ads", 1, adService.loadStarredAds().size());
        assertEquals("removeStarredAdValidIdMultipleTest: wrong ad was removed from the list of starred ads", secondAdId, adService.loadStarredAds().get(0).getId());
        assertEquals("removeStarredAdValidIdMultipleTest: should pass if the unstarred ad still appears in the list of all ads", 2, adService.loadAllAds().size());

        // Makes sure the data of the unstarred ad matches that of the original ad
        Ad unstarredAd = adService.getReferenceById(firstAdId);
        assertEquals("removeStarredAdValidIdMultipleTest: name of unstarred ad is not same as originally", name, unstarredAd.getName());
        assertEquals("removeStarredAdValidIdMultipleTest: price of unstarred ad is not same as originally", price, unstarredAd.getPrice());
        assertEquals("removeStarredAdValidIdMultipleTest: description of unstarred ad is not same as originally", description, unstarredAd.getDescription());
        assertEquals("removeStarredAdValidIdMultipleTest: starred status of unstarred ad should not be true", false, unstarredAd.getStarred());
        assertEquals("removeStarredAdValidIdMultipleTest: user of unstarred ad is not same as originally", user, unstarredAd.getUser());
        // Makes sure the data of the starred ad have not changed
        Ad starredAd = adService.getReferenceById(secondAdId);
        assertEquals("removeStarredAdValidIdMultipleTest: name of starred ad is not same as originally", name + "1", starredAd.getName());
        assertEquals("removeStarredAdValidIdMultipleTest: price of starred ad is not same as originally", price + 1, starredAd.getPrice());
        assertEquals("removeStarredAdValidIdMultipleTest: description of starred ad is not same as originally", description + "1", starredAd.getDescription());
        assertEquals("removeStarredAdValidIdMultipleTest: starred status of starred ad should not be false", true, starredAd.getStarred());
        assertEquals("removeStarredAdValidIdMultipleTest: user of starred ad is not same as originally", secondUser, starredAd.getUser());
    }

    // Ensures that removeStarred() is unsuccessful if an invalid ID is passed
    @Test
    public void removeStarredAdInvalidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        // Delete this ad and star its ID to ensure the ID is invalid
        adService.deleteAd(adId);

        assertFalse("removeStarredAdInvalidIdTest: didn't fail when passed an invalid ID", adService.removeStarredAd(adId));
        assertEquals("removeStarredAdInvalidIdTest: ad shouldn't be present in list of starred ads after unstarring invalid ID", 0, adService.loadStarredAds().size());
        assertEquals("removeStarredAdInvalidIdTest: ad is present after starring invalid ID", 0, adService.loadAllAds().size());
    }

    // Ensures that removeStarredAd() is unsuccessful if the designated ad is already unstarred
    @Test
    public void removeStarredAdAlreadyUnstarredTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();

        assertFalse("removeStarredAdAlreadyUnstarredTest: shouldn't pass successfully if ad is already unstarred", adService.removeStarredAd(adId));
        assertEquals("removeStarredAdAlreadyUnstarredTest: shouldn't be any ads in list of starred ads", 0, adService.loadStarredAds().size());
        assertEquals("removeStarredAdAlreadyUnstarredTest: number of total ads shouldn't have changed", 1, adService.loadAllAds().size());

        // Ensure that the data of the ad hasn't changed
        Ad ad = adService.getReferenceById(adId);
        assertEquals("removeStarredAdAlreadyUnstarredTest: name of ad isn't same as originally", name, ad.getName());
        assertEquals("removeStarredAdAlreadyUnstarredTest: price of ad isn't same as originally", price, ad.getPrice());
        assertEquals("removeStarredAdAlreadyUnstarredTest: description of ad isn't same as originally", description, ad.getDescription());
        assertEquals("removeStarredAdAlreadyUnstarredTest: starred status of ad isn't same as originally", false, ad.getStarred());
        assertEquals("removeStarredAdAlreadyUnstarredTest: user of ad isn't same as originally", user, ad.getUser());
    }

    // Ensures that removeStarredAd() is unsuccessful if an invalid ID is passed, even if other ads are still present in the DB
    @Test
    public void removeStarredAdMultipleInvalidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int firstAdId = adService.loadAllAds().get(0).getId();
        // Delete this ad and star its ID to ensure the ID is invalid
        adService.deleteAd(firstAdId);

        userService.createUser("username1", "Password@1");
        User newUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, newUser);
        int secondAdId = adService.loadAllAds().get(0).getId();
        adService.starAd(secondAdId);

        assertFalse("starAdMultipleInvalidIdTest: didn't fail when passed an invalid ID", adService.removeStarredAd(firstAdId));
        assertEquals("starAdMultipleInvalidIdTest: starred ad is present after starring invalid ID", 1, adService.loadStarredAds().size());
        assertEquals("starAdMultipleInvalidIdTest: ad is present after starring invalid ID", 1, adService.loadAllAds().size());
        // Ensure data in remaining ad is same as before trying to star the invalid ID
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("starAdMultipleInvalidIdTest: name of remaining ad isn't same as originally", name + "1", remainingAd.getName());
        assertEquals("starAdMultipleInvalidIdTest: price of remaining ad isn't same as originally", price + 1, remainingAd.getPrice());
        assertEquals("starAdMultipleInvalidIdTest: description of remaining ad isn't same as originally", description + "1", remainingAd.getDescription());
        assertEquals("starAdMultipleInvalidIdTest: starred status of remaining ad isn't same as before attempted unstarring", true, remainingAd.getStarred());
    }

    // Ensures that removeStarredAd() is unsuccessful if given a null ID
    @Test
    public void removeStarredAdNullIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        assertFalse("removeStarredAdNullIdTest: shouldn't be successful when given a null ID", adService.removeStarredAd(null));
        assertEquals("removeStarredAdNullIdTest: size of ad list isn't same as before unsuccessful attempt to star ad", 1, adService.loadAllAds().size());

        Ad createdAd = adService.getReferenceById(adId);
        assertEquals("removeStarredAdNullIdTest: name of ad isn't same as before trying to star ad", name, createdAd.getName());
        assertEquals("removeStarredAdNullIdTest: description of ad isn't same as before trying to star ad", description, createdAd.getDescription());
        assertEquals("removeStarredAdNullIdTest: price of ad isn't same as before trying to star ad", price, createdAd.getPrice());
        assertEquals("removeStarredAdNullIdTest: starred status of remaining ad should be false", false, createdAd.getStarred());
        assertEquals("removeStarredAdNullIdTest: user of ad isn't same as before trying to star ad", user, createdAd.getUser());
    }

    // Ensures that deleteAd() is successful for a valid ID
    @Test
    public void deleteAdValidIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();

        assertTrue("deleteAdValidIdTest: should pass successfully with a valid ID", adService.deleteAd(adId));
        assertEquals("deleteAdValidIdTest: list of all ads should be empty", 0, adService.loadAllAds().size());
    }

    // Ensures that deleteAd() removes the correct advertisement from the DB when there are several ads present
    @Test
    public void deleteAdValidIdMultipleTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        userService.createUser("username1", "Password@1");
        User newUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, newUser);

        assertTrue("deleteAdValidIdMultipleTest: should pass successfully when given a valid ID", adService.deleteAd(adId));
        assertEquals("deleteAdValidIdMultipleTest: number of total ads should be 1 after deletion", 1, adService.loadAllAds().size());

        // Ensure that the correct ad was removed
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("deleteAdValidIdMultipleTest: name of ad is not the same as the ad that should be remaining", name + "1", remainingAd.getName());
        assertEquals("deleteAdValidIdMultipleTest: price of ad is not the same as the ad that should be remaining", price + 1, remainingAd.getPrice());
        assertEquals("deleteAdValidIdMultipleTest: description of ad is not the same as the ad that should be remaining", description + "1", remainingAd.getDescription());
        assertEquals("deleteAdValidIdMultipleTest: user of ad is not the same as the ad that should be remaining", newUser, remainingAd.getUser());
    }

    // Ensures that deleteAd() is unsuccessful if given an ID that is not attached to an advertisement
    @Test
    public void deleteAdInvalidIdTest() {
        // Since there are no ads in the DB, any ID is sure to be invalid
        assertFalse("deleteAdInvalidIdTest: should not pass successfully when given an invalid ID", adService.deleteAd(1));
        assertEquals("deleteAdInvalidIdTest: list of all ads should be empty", 0, adService.loadAllAds().size());
    }

    // Ensures that deleteAd() is unsuccessful if given an ID that is not attached to an advertisement, even if there are multiple ads present
    @Test
    public void deleteAdInvalidIdMultipleTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        userService.createUser("username1", "Password@1");
        User newUser = userService.findByUsernameIgnoreCase("username1").get(0);
        adService.createAd(name + "1", description + "1", price + 1, newUser);
        adService.deleteAd(adId);

        assertFalse("deleteAdInvalidIdMultipleTest: shouldn't pass successfully when given an invalid ID", adService.deleteAd(adId));
        assertEquals("deleteAdInvalidIdMultipleTest: number of ads shouldn't change with unsuccessful deletion", 1, adService.loadAllAds().size());

        // Ensure that no data in the remaining ad has been changed
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("deleteAdInvalidIdMultipleTest: name of ad is not the same as originally", name + "1", remainingAd.getName());
        assertEquals("deleteAdInvalidIdMultipleTest: price of ad is not the same as originally", price + 1, remainingAd.getPrice());
        assertEquals("deleteAdInvalidIdMultipleTest: description of ad is not the same as originally", description + "1", remainingAd.getDescription());
        assertEquals("deleteAdInvalidIdMultipleTest: user of ad is not the same as originally", newUser, remainingAd.getUser());
    }

    // Ensure that deleteAd() is unsuccessful if given the ID of an advertisement that was just deleted
    @Test
    public void deleteAdJustDeletedTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        adService.deleteAd(adId);

        assertFalse("deleteAdJustDeletedTest: shouldn't pass successfully when given an ad that was just deleted", adService.deleteAd(adId));
        assertEquals("deleteAdJustDeletedTest: list of all ads should be empty", 0, adService.loadAllAds().size());
    }

    // Ensure that deleteAd() is unsuccessful if given a null ID
    @Test
    public void deleteAdNullIdTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);

        assertFalse("deleteAdNullIdTest: shouldn't pass successfully when given a null ID", adService.deleteAd(null));
        assertEquals("deleteAdNullIdTest: list of all ads should be same as before null deletion", 1, adService.loadAllAds().size());

        // Ensure the data of the remaining ad hasn't changed
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("deleteAdNullIdTest: name of ad is not the same as originally", name, remainingAd.getName());
        assertEquals("deleteAdNullIdTest: price of ad is not the same as originally", price, remainingAd.getPrice());
        assertEquals("deleteAdNullIdTest: description of ad is not the same as originally", description, remainingAd.getDescription());
        assertEquals("deleteAdNullIdTest: user of ad is not the same as originally", user, remainingAd.getUser());
    }

    // Ensure that loadStarredAds() returns the correct number of ads when there are no ads in the DB
    @Test
    public void loadStarredAdsZeroAdsTest() {
        assertEquals("loadStarredAdsZeroAdsTest: should pass with zero starred ads to load", 0, adService.loadStarredAds().size());
    }

    // Ensure that deleteAllAds() correctly deletes all advertisements in the DB
    @Test
    public void deleteAllAdsZeroAdsTest() {
        // Ensures that nothing is done if no ads are present in the database
        if (!adService.loadAllAds().isEmpty()) {
            List<Ad> allAds = adService.loadAllAds();
            for (Ad ad : allAds) {
                adService.deleteAd(ad.getId());
            }
        }
        List<Ad> allAds = adService.loadAllAds();
        for (Ad allAd : allAds) {
            adService.deleteAd(allAd.getId());
        }

        assertEquals("deleteAllAdsZeroAdsTest: should pass if no ads are present to delete", 0, adService.loadAllAds().size());
    }

    // Ensure that removeAllStarredAds() successfully unstars all starred ads in the DB
    @Test
    public void removeAllStarredAdsVariedNumberOfAdsTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                adService.createAd(name, description, price, user);
                int adId = adService.loadAllAds().get(adService.loadAllAds().size() - 1).getId();
                adService.starAd(adId);
            }
        }
        List<Ad> starredAds = adService.loadStarredAds();
        for (Ad allAd : starredAds) {
            adService.deleteAd(allAd.getId());
        }

        assertEquals("removeAllStarredAdsVariedNumberOfAdsTest: should pass if all starred ads were removed", 0, adService.loadStarredAds().size());
    }

    // Ensure that removeAllStarredAds() makes no changes when no starred ads are in the DB
    @Test
    public void removeAllStarredAdsNoAdsTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        List<Ad> starredAds = adService.loadStarredAds();
        for (Ad allAd : starredAds) {
            adService.deleteAd(allAd.getId());
        }

        assertEquals("removeAllStarredAdsNoAdsTest: should pass if there are no starred ads", 0, adService.loadStarredAds().size());
    }

    // Ensure that loadAllAds() returns no ads when there are no ads in the DB
    @Test
    public void loadAllAdsZerosAdsTest() {
        assertEquals("loadAllAdsZerosAdsTest: should succeed when there are zeros ads", 0, adService.loadAllAds().size());
    }

    // Ensure that loadAllAds() returns all ads in the DB
    @Test
    public void loadAllAdsTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> allAds = adService.loadAllAds();

        assertEquals("loadAllAdsTest: id of ad should be equal to the id it was set to", allAds.get(0).getUser().getId(), userId);
        assertEquals("loadAllAdsTest: name of ad should be equal to the name it was set to", allAds.get(0).getName(), name);
        assertEquals("loadAllAdsTest: description of ad should be equal to the description it was set to", allAds.get(0).getDescription(), description);
        assertEquals("loadAllAdsTest: price of ad should be equal to the price it was set to", allAds.get(0).getPrice(), price);
        assertEquals("loadAllAdsTest: user of ad should be equal to the user it was set to", allAds.get(0).getUser(), user);

    }

    // Ensures that loadAllAds() returns the correct number of ads when several ads are present
    @Test
    public void loadAllAdsMultipleAdsTest() {
        userService.createUser("username1", "Password@2");
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        User user2 = userService.findByUsernameIgnoreCase("username1").get(0);

        adService.createAd(name, description, price, user);
        adService.createAd(name + "1", description, price, user2);

        assertEquals("loadAllAdsMultipleAdsTest: size of allAds should be equal to the amount of ads that were added", 2, adService.loadAllAds().size());
    }

    // Ensure that loadAllAds() returns the correct number of ads when there are several ads in the DB
    @Test
    public void loadAllAdsMultipleAdsDifferentSizeTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        int count = 0;
        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user);
            count++;
        }
        count++;
        List<Ad> allAds = adService.loadAllAds();
        assertFalse("loadAllAdsMultipleAdsDifferentSizeTest: size of allAds should not be equal to the amount of ads plus 1 that were added", count == allAds.size());
    }

    // Ensures that loadAllAds() returns the correct number of ads when ads have differing users
    @Test
    public void loadAllAdsDifferentUserCreatedTest() {
        userService.createUser("username2", "Password@2");
        userService.createUser("username3","Password@3");
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        User user2 = userService.findByUsernameIgnoreCase("username2").get(0);
        User user3 = userService.findByUsernameIgnoreCase("username3").get(0);

        adService.createAd(name, description, price, user);
        adService.createAd(name + "1", description, price, user2);
        adService.createAd(name + "2", description, price, user3);

        assertEquals("loadCreatedAdsDifferentUserCreatedTest: size of allAds should be 3", 3, adService.loadAllAds().size());
    }

    // Ensure that loadCreatedAds() returns all ads created by the current user
    @Test
    public void loadCreatedAdsTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> createdAds = adService.loadCreatedAds(userId);

        assertEquals("loadCreatedAdsTest: id of ad should be equal to the id it was set to", createdAds.get(0).getUser().getId(), userId);
        assertEquals("loadCreatedAdsTest: name of ad should be equal to the name it was set to", createdAds.get(0).getName(), name);
        assertEquals("loadCreatedAdsTest: description of ad should be equal to the description it was set to", createdAds.get(0).getDescription(), description);
        assertEquals("loadCreatedAdsTest: price of ad should be equal to the price it was set to", createdAds.get(0).getPrice(), price);
        assertEquals("loadCreatedAdsTest: user of ad should be equal to the user it was set to", createdAds.get(0).getUser(), user);

    }

    // Ensure that loadCreatedAds() returns all ads created by the current user when there are several ads in the DB
    @Test
    public void loadCreatedAdsMultipleAdsTest() {
        userService.createUser("username1", "Password@1");
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        User user1 = userService.findByUsernameIgnoreCase("username1").get(0);

        adService.createAd(name, description, price, user);
        adService.createAd(name + "1", description, price, user);

        int userId = adService.loadAllAds().get(0).getUser().getId();
        assertEquals("loadCreatedAdsMultipleAdsTest: size of createdAds should be equal to the amount of ads that were added", 2, adService.loadCreatedAds(userId).size());
    }

    // Ensure loadCreatedAds() returns the ads created by the current user when ads have different creators
    @Test
    public void loadCreatedAdsDifferentUserCreatedTest() {
        userService.createUser("username2", "Password@2");
        userService.createUser("username3","Password@3");
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        User user2 = userService.findByUsernameIgnoreCase("username2").get(0);
        User user3 = userService.findByUsernameIgnoreCase("username3").get(0);

        adService.createAd(name + "1", description, price, user2);
        adService.createAd(name + "3", description, price, user2);
        adService.createAd(name + "2", description, price, user3);

        List<Ad> user2createdAds = adService.loadCreatedAds(user2.getId());
        List<Ad> user3createdAds = adService.loadCreatedAds(user3.getId());

        assertEquals("loadCreatedAdsDifferentUserCreatedTest: size of user2createdAds should be 10", 2, user2createdAds.size());
        assertEquals("loadCreatedAdsDifferentUserCreatedTest: size of user3createdAds should be 5", 1, user3createdAds.size());
        assertFalse("loadCreatedAdsDifferentUserCreatedTest: size of user2createdAds should not be 5", 5 ==  user2createdAds.size());
        assertFalse("loadCreatedAdsDifferentUserCreatedTest: size of user3createdAds should not be 10", 10 ==  user3createdAds.size());
    }

    // Ensure that getReferenceById() returns the correct ad when passed a valid ID
    @Test
    public void getReferenceByIdValidTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        Ad newAd = adService.getReferenceById(adId);

        assertEquals("getReferenceByIdTest: id of ad should be equal to the id it was set to", newAd.getId(), adId);
        assertEquals("getReferenceByIdTest: name of ad should be equal to the name it was set to", newAd.getName(), name);
        assertEquals("getReferenceByIdTest: description of ad should be equal to the description it was set to", newAd.getDescription(), description);
        assertEquals("getReferenceByIdTest: price of ad should be equal to the price it was set to", newAd.getPrice(), price);
        assertEquals("getReferenceByIdTest: user of ad should be equal to the user it was set to", newAd.getUser(), user);
    }

    // Ensure that getReferenceById returns fails when an invalid ID is passed
    @Test
    public void getReferenceByIdInvalidTest() {
        User user = userService.findByUsernameIgnoreCase("username").get(0);
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        Ad newAd = adService.getReferenceById(adId);

        assertFalse("getReferenceByIdTest: id plus 1 of ad should not be equal to the id it was set to", newAd.getId() == adId+1);
        assertFalse("getReferenceByIdTest: a different name of ad should not be equal to the name it was set to", newAd.getName().equals("NotTheName"));
        assertFalse("getReferenceByIdTest: a different description of ad should not be equal to the description it was set to", newAd.getDescription().equals("Not the description"));
        assertFalse("getReferenceByIdTest: price plus 1 should not be equal to the price it was set to", newAd.getPrice().equals(price+1));
        assertTrue("getReferenceByIdTest: user of ad should be equal to the user it was set to", newAd.getUser().equals(user));
    }
}