package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class AdServiceImplTest {

    private final String name = "testname";
    private final Float price = 5.0F;
    private final String description = "testdescription";

    @Autowired
    private AdService adService;

    @Autowired
    private UserService userService;

    @BeforeEach
    public void beforeTest() {
        userService.createUser("Username1", "Password@1");
        userService.createUser("Username2", "Password@1");
        userService.createUser("Username3", "Password@1");
    }

    // Tests to ensure createAd() method creates and stores a single advertisement correctly when passed valid data
    @Test
    public void createAdValidDataTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("createAdValidDataTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        List<Ad> allCreatedAds = adService.loadAllAds();

        assertEquals("createAdValidDataTest: should create a single ad with valid data", 1, allCreatedAds.size());
        Ad createdAd = allCreatedAds.get(0);

        assertEquals("createAdValidDataTest: name should be stored correctly with valid ad data", name, createdAd.getName());
        assertEquals("createAdValidDataTest: price should be stored correctly with valid ad data", price, createdAd.getPrice());
        assertEquals("createAdValidDataTest: description should be stored correctly with valid ad data", description, createdAd.getDescription());
        assertEquals("createAdValidDataTest: userId should be stored correctly with valid ad data", user_id, createdAd.getUser().getId());
    }

    // Tests to ensure createAd() method creates and stores several valid advertisements correctly
    @Test
    public void createMultipleAdsValidDataTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("createMultipleAdsValidDataTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();
        assertTrue("createMultipleAdsValidDataTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user2_id));

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
                assertEquals("createMultipleAdsValidDataTest: second ad doesn't contain correct user", user2_id, ad.getUser().getId());
            }
        }
    }

    // Tests to ensure createAd() method creates and stores a single advertisement correctly when all data is the same as
    // another ad except for the ID of the user creating it
    @Test
    public void createAdDuplicateAdDifferentUserIdTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();


        assertTrue("createAdDuplicateAdDifferentUserIdTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        List<Ad> allCreatedAds = adService.loadAllAds();
        assertEquals("createAdDuplicateAdDifferentUserIdTest: should create a single ad with valid data", 1, allCreatedAds.size());

        assertTrue("createAdDuplicateAdDifferentUserIdTest: createAd() should return true with valid data", adService.createAd(name, description, price, user2_id));
        allCreatedAds = adService.loadAllAds();
        assertEquals("createAdDuplicateAdDifferentUserIdTest: should create a single duplicate ad with different user ID", 2, allCreatedAds.size());

        List<Ad> duplicateAds = adService.loadCreatedAds(user.getId());
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("createAdDuplicateAdSameUserIdTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertFalse("createAdDuplicateAdSameUserIdTest: attempted duplicate ad should fail when having the same user ID", adService.createAd(name, description, price, user_id));
    }

    // Tests to ensure that createAd() will fail if no name is provided
    @Test
    public void createAdNoNameTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertFalse("createAdNoNameTest: should not create successfully when no name is supplied", adService.createAd("", description, price, user_id));
        assertEquals("createAdNoNameTest: should not create an ad when no name is supplied", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() will create an ad even if no description is provided
    @Test
    public void createAdNoDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        assertTrue("createAdNoDescriptionTest: should create an ad when no description is supplied", adService.createAd(name, "", price, user_id));
        assertEquals("createAdNoDescriptionTest: should create an ad when no description is supplied", 1, adService.loadAllAds().size());

        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("createAdNoDescriptionTest: name should be stored correctly without providing a description", name, createdAd.getName());
        assertEquals("createAdNoDescriptionTest: price should be stored correctly without providing a description", price, createdAd.getPrice());
        assertEquals("createAdNoDescriptionTest: empty description should be stored correctly", "", createdAd.getDescription());
        assertEquals("createAdNoDescriptionTest: user should be stored correctly without providing a description", user_id, createdAd.getUser().getId());
    }

    // Tests to ensure that createAd() will fail if the provided user is not in the DB
    @Test
    public void createAdInvalidUserTest() {
        assertFalse("createAdNoUserTest: should not create successfully when no valid user is supplied", adService.createAd("", description, price, -1));
        assertEquals("createAdNoUserTest: should not create an ad when no user is supplied", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() creates an ad correctly with a price of 0
    @Test
    public void createAdPriceZeroTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("createAdPriceZeroTest: createAd() should return true with valid data", adService.createAd(name, description, (float) 0, user_id));
        assertEquals("createAdPriceZeroTest: should pass with a price of $0.00", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("createAdPriceZeroTest: name not stored correctly for ad with price of $0.00", name, createdAd.getName());
        assertEquals("createAdPriceZeroTest: description not stored correctly for ad with price of $0.00", description, createdAd.getDescription());
        assertEquals("createAdPriceZeroTest: price not stored correctly for ad with price of $0.00", (float) 0, createdAd.getPrice());
        assertEquals("createAdPriceZeroTest: user not stored correctly for ad with price of $0.00", user_id, createdAd.getUser().getId());
    }

    // Tests to ensure that createAd() creates an ad correctly with a price of 0
    @Test
    public void createAdNegativePriceTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertFalse("createAdNegativePriceTest: createAd() should return false with negative price", adService.createAd(name, description, (float) -1, user_id));
        assertEquals("createAdNegativePriceTest: shouldn't pass with a price of -$1.00", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when passed a null name
    @Test
    public void createAdNullNameTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertFalse("createAdNullNameTest: should return false when passed null name", adService.createAd(null, description, price, user_id));
        assertEquals("createAdNullNameTest: should fail with null name", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when passed a null price
    @Test
    public void createAdNullPriceTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertFalse("createAdNullPriceTest: should return false when passed null price", adService.createAd(name, description, null, user_id));
        assertEquals("createAdNullPriceTest: should pass with null price", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() creates an ad when passed a null description
    @Test
    public void createAdNullDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("createAdNullDescriptionTest: should return true when passed null description", adService.createAd(name, null, price, user_id));
        assertEquals("createAdNullNameTest: should pass with null name", 1, adService.loadAllAds().size());
    }


    // Tests to ensure that createAd() doesn't create an ad when all parameters are null
    @Test
    public void createAdNullAllTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertFalse("createAdNullAllTest: should return false when passed all null values", adService.createAd(null, null, null, 0));
        assertEquals("createAdNullAllTest: should fail with all Null data", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that editAd() correctly changes all the data of an advertisement if given valid data
    @Test
    public void editAdValidDataAllChangeTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdValidDataAllChangeTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdValidDataTest: should return true when valid data is given", adService.editAd(name + "1", description + "1", price + 1, adService.loadAllAds().get(0).getId(), user_id));
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdValidDataNameChangeTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name + "1", description, price, adService.loadAllAds().get(0).getId(), user_id));
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdValidDataPriceChangeTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name, description, price + 1, adService.loadAllAds().get(0).getId(), user_id));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name, editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price + 1, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description, editedAd.getDescription());
        assertEquals("editAdValidDataTest: user was changed when the ad was edited when it the user should have remained the same", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes just the description of an advertisement if given valid data
    @Test
    public void editAdValidDataDescriptionChangeTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdValidDataDescriptionChangeTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name, description + "1", price, adService.loadAllAds().get(0).getId(), user_id));
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdEmptyNameTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertFalse("editAdEmptyNameTest: shouldn't be successful when name is changed to be empty", adService.editAd("", description, price, adService.loadAllAds().get(0).getId(), user_id));
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdEmptyNameTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdEmptyDescriptionTest: should be successful when description is changed to be empty", adService.editAd(name, "", price, adService.loadAllAds().get(0).getId(), user_id));
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdNullNameTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertFalse("editAdNullNameTest: shouldn't be successful when given name is null", adService.editAd(null, description, price, adService.loadAllAds().get(0).getId(), user_id));
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdNullPriceTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertFalse("editAdNullPriceTest: should be unsuccessful when given price is null", adService.editAd(name, description, null, adService.loadAllAds().get(0).getId(), user_id));
        assertEquals("editAdNullPriceTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullPriceTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullPriceTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullPriceTest: description of ad wasn't changed correctly", description, createdAd.getDescription());
        assertEquals("editAdNullPriceTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

    // Tests to ensure that editAd() is successful if the supplied description is null
    @Test
    public void editAdNullDescriptionTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdNullDescriptionTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdNullDescriptionTest: should be successful when given description is null", adService.editAd(name, null, price, adService.loadAllAds().get(0).getId(), user_id));
        assertEquals("editAdNullDescriptionTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdNullDescriptionTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdNullDescriptionTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdNullDescriptionTest: description of ad wasn't changed correctly", null, createdAd.getDescription());
        assertEquals("editAdNullDescriptionTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }



    // Tests to ensure that editAd() is unsuccessful if all supplied parameters are null
    @Test
    public void editAdNullAllTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        adService.createAd(name, description, price, user_id);
        assertFalse("editAdNullAllTest: shouldn't be successful when given description is null", adService.editAd(null, null, null, null, user_id));
    }


    // Tests to ensure that editAd() is unsuccessful if given an ID that is doesn't map to an advertisement
    @Test
    public void editAdInvalidIdTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("editAdInvalidIdTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("editAdInvalidIdTest: createAd() should return true with valid data", adService.createAd(name+"1", description, price, user_id));
        int deletedAdId = 0;
        for (Ad ad : adService.loadAllAds()) {
            if (ad.getName().equals(name + "1")) {
                deletedAdId = ad.getId();
                adService.deleteAd(deletedAdId);
                break;
            }
        }
        Ad remainingAd = adService.loadAllAds().get(0);
        assertFalse("editAdInvalidIdTest: shouldn't be successful when given ID matches no advertisement", adService.editAd(name, description, price, deletedAdId, user_id));
        assertEquals("editAdInvalidIdTest: name of ad isn't same as before unsuccessful editing", name, remainingAd.getName());
        assertEquals("editAdInvalidIdTest: price of ad isn't same as before unsuccessful editing", price, remainingAd.getPrice());
        assertEquals("editAdInvalidIdTest: description of ad wasn't changed correctly", description, remainingAd.getDescription());
        assertEquals("editAdInvalidIdTest: user of ad isn't same as before unsuccessful editing", user, remainingAd.getUser());
    }

    // Ensures that deleteAd() is successful for a valid ID
    @Test
    public void deleteAdValidIdTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("deleteAdValidIdTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        int adId = adService.loadAllAds().get(0).getId();
        assertTrue("deleteAdValidIdTest: should pass successfully with a valid ID", adService.deleteAd(adId));
        assertEquals("deleteAdValidIdTest: list of all ads should be empty", 0, adService.loadAllAds().size());
    }

    // Ensures that deleteAd() removes the correct advertisement from the DB when there are several ads present
    @Test
    public void deleteAdValidIdMultipleTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("deleteAdValidIdMultipleTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        int adId = adService.loadAllAds().get(0).getId();

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();
        assertTrue("deleteAdValidIdMultipleTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user2_id));

        assertTrue("deleteAdValidIdMultipleTest: should pass successfully when given a valid ID", adService.deleteAd(adId));
        assertEquals("deleteAdValidIdMultipleTest: number of total ads should be 1 after deletion", 1, adService.loadAllAds().size());

        // Ensure that the correct ad was removed
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("deleteAdValidIdMultipleTest: name of ad is not the same as the ad that should be remaining", name + "1", remainingAd.getName());
        assertEquals("deleteAdValidIdMultipleTest: price of ad is not the same as the ad that should be remaining", price + 1, remainingAd.getPrice());
        assertEquals("deleteAdValidIdMultipleTest: description of ad is not the same as the ad that should be remaining", description + "1", remainingAd.getDescription());
        assertEquals("deleteAdValidIdMultipleTest: user of ad is not the same as the ad that should be remaining", user2, remainingAd.getUser());
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
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("deleteAdInvalidIdMultipleTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        int adId = adService.loadAllAds().get(0).getId();

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();
        assertTrue("deleteAdInvalidIdMultipleTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user2_id));
        assertTrue("deleteAdInvalidIdMultipleTest: deleteAd() should return true with valid ID", adService.deleteAd(adId));

        assertFalse("deleteAdInvalidIdMultipleTest: shouldn't pass successfully when given an invalid ID", adService.deleteAd(adId));
        assertEquals("deleteAdInvalidIdMultipleTest: number of ads shouldn't change with unsuccessful deletion", 1, adService.loadAllAds().size());

        // Ensure that no data in the remaining ad has been changed
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("deleteAdInvalidIdMultipleTest: name of ad is not the same as originally", name + "1", remainingAd.getName());
        assertEquals("deleteAdInvalidIdMultipleTest: price of ad is not the same as originally", price + 1, remainingAd.getPrice());
        assertEquals("deleteAdInvalidIdMultipleTest: description of ad is not the same as originally", description + "1", remainingAd.getDescription());
        assertEquals("deleteAdInvalidIdMultipleTest: user of ad is not the same as originally", user2, remainingAd.getUser());
    }

    // Ensure that deleteAd() is unsuccessful if given the ID of an advertisement that was just deleted
    @Test
    public void deleteAdJustDeletedTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("deleteAdJustDeletedTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));

        int adId = adService.loadAllAds().get(0).getId();
        assertTrue("deleteAdJustDeletedTest: deleteAd() should return true with valid ID", adService.deleteAd(adId));

        assertFalse("deleteAdJustDeletedTest: shouldn't pass successfully when given an ad that was just deleted", adService.deleteAd(adId));
        assertEquals("deleteAdJustDeletedTest: list of all ads should be empty", 0, adService.loadAllAds().size());
    }

    // Ensure that deleteAd() is unsuccessful if given a null ID
    @Test
    public void deleteAdNullIdTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        adService.createAd(name, description, price, user_id);

        assertFalse("deleteAdNullIdTest: shouldn't pass successfully when given a null ID", adService.deleteAd(-1));
        assertEquals("deleteAdNullIdTest: list of all ads should be same as before null deletion", 1, adService.loadAllAds().size());

        // Ensure the data of the remaining ad hasn't changed
        Ad remainingAd = adService.loadAllAds().get(0);
        assertEquals("deleteAdNullIdTest: name of ad is not the same as originally", name, remainingAd.getName());
        assertEquals("deleteAdNullIdTest: price of ad is not the same as originally", price, remainingAd.getPrice());
        assertEquals("deleteAdNullIdTest: description of ad is not the same as originally", description, remainingAd.getDescription());
        assertEquals("deleteAdNullIdTest: user of ad is not the same as originally", user, remainingAd.getUser());
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

    // Ensure that loadAllAds() returns no ads when there are no ads in the DB
    @Test
    public void loadAllAdsZerosAdsTest() {
        assertEquals("loadAllAdsZerosAdsTest: should succeed when there are zeros ads", 0, adService.loadAllAds().size());
    }

    // Ensure that loadAllAds() returns a single ad in the DB
    @Test
    public void loadAllAdsOneAdTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();
        assertTrue("loadAllAdsOneAdTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        List<Ad> allAds = adService.loadAllAds();
        assertEquals("loadAllAdsOneAdTest: should be 1 ad in the DB", 1, allAds.size());
        Ad ad = allAds.get(0);

        assertEquals("loadAllAdsOneAdTest: name of ad should be equal to the name it was set to", ad.getName(), name);
        assertEquals("loadAllAdsOneAdTest: description of ad should be equal to the description it was set to", ad.getDescription(), description);
        assertEquals("loadAllAdsOneAdTest: price of ad should be equal to the price it was set to", ad.getPrice(), price);
        assertEquals("loadAllAdsOneAdTest: user of ad should be equal to the user it was set to", ad.getUser(), user);

    }

    // Ensures that loadAllAds() returns all ads when several ads are present, created by the same user
    @Test
    public void loadAllAdsMultipleAdsSameUserTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        assertTrue("loadAllAdsMultipleAdsTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("loadAllAdsMultipleAdsTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user_id));

        List<Ad> allAds = adService.loadAllAds();
        assertEquals("loadAllAdsMultipleAdsTest: size of allAds should be equal to the amount of ads that were added", 2, allAds.size());
        for (Ad ad : allAds) {
            assertTrue("loadAllAdsMultipleAdsTest: name of ad in list of all ads is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1"));
            if (ad.getName().equals(name)) {
                assertEquals("loadAllAdsMultipleAdsTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("loadAllAdsMultipleAdsTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("loadAllAdsMultipleAdsTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("loadAllAdsMultipleAdsTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("loadAllAdsMultipleAdsTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("loadAllAdsMultipleAdsTest: second ad doesn't contain correct user", user, ad.getUser());
            }
        }
    }

    // Ensures that loadAllAds() returns all ads when several ads are present, created by the same user
    @Test
    public void loadAllAdsMultipleAdsDifferentUserTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();

        assertTrue("loadAllAdsMultipleAdsTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("loadAllAdsMultipleAdsTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user2_id));

        List<Ad> allAds = adService.loadAllAds();
        assertEquals("loadAllAdsMultipleAdsTest: size of allAds should be equal to the amount of ads that were added", 2, allAds.size());
        for (Ad ad : allAds) {
            assertTrue("loadAllAdsMultipleAdsTest: name of ad in list of all ads is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1"));
            if (ad.getName().equals(name)) {
                assertEquals("loadAllAdsMultipleAdsTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("loadAllAdsMultipleAdsTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("loadAllAdsMultipleAdsTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("loadAllAdsMultipleAdsTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("loadAllAdsMultipleAdsTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("loadAllAdsMultipleAdsTest: second ad doesn't contain correct user", user2, ad.getUser());
            }
        }
    }


    // Ensures that loadCreatedAds() returns all ads created by a user, when multiple are present
    @Test
    public void loadCreatedAdsMultipleAdsSameUserTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        assertTrue("loadCreatedAdsMultipleAdsSameUserTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("loadCreatedAdsMultipleAdsSameUserTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user_id));

        List<Ad> allAds = adService.loadCreatedAds(user.getId());
        assertEquals("loadCreatedAdsMultipleAdsSameUserTest: size of createdAds should be equal to the amount of ads that were added", 2, allAds.size());
        for (Ad ad : allAds) {
            assertTrue("loadCreatedAdsMultipleAdsSameUserTest: name of ad in list of all ads is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1"));
            if (ad.getName().equals(name)) {
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct user", user, ad.getUser());
            }
        }
    }

    // Ensures that loadCreatedAds() returns all ads created by a user, when multiple users have both created a single ad
    @Test
    public void loadCreatedAdsOneAdsDifferentUsersTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();


        assertTrue("loadCreatedAdsOneAdsDifferentUsersTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("loadCreatedAdsOneAdsDifferentUsersTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user2_id));

        List<Ad> createdAds = adService.loadCreatedAds(user.getId());
        assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: size of createdAds should be equal to 1", 1, createdAds.size());
        Ad createdAd = createdAds.get(0);
        assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: name of ad in list of all ads is different than either of the names of the created ads", name, createdAd.getName());
        assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: loaded ad doesn't contain same description as when created", description, createdAd.getDescription());
        assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: loaded ad doesn't contain same price as when created", price, createdAd.getPrice());
        assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: loaded ad doesn't contain same user as when created", user, createdAd.getUser());

        // Ensure both created ads are still present in the DB
        List<Ad> allAds = adService.loadAllAds();
        assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: size of allAds should be equal to 2", 2, allAds.size());
        for (Ad ad : allAds) {
            assertTrue("loadCreatedAdsOneAdsDifferentUsersTest: name of ad in list of all ads is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1"));
            if (ad.getName().equals(name)) {
                assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("loadCreatedAdsOneAdsDifferentUsersTest: second ad doesn't contain correct user", user2, ad.getUser());
            }
        }
    }

    // Ensures that loadCreatedAds() returns all ads created by a user, when multiple users have created multiple ads
    @Test
    public void loadCreatedAdsMultipleAdsDifferentUsersTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        User user2 = userService.findByUsernameIgnoreCase("Username2").get(0);
        int user2_id = user2.getId();

        assertTrue("loadCreatedAdsMultipleAdsDifferentUsersTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        assertTrue("loadCreatedAdsMultipleAdsDifferentUsersTest: createAd() should return true with valid data", adService.createAd(name + "1", description + "1", price + 1, user_id));
        assertTrue("loadCreatedAdsMultipleAdsDifferentUsersTest: createAd() should return true with valid data", adService.createAd(name + "2", description + "2", price + 2, user2_id));
        assertTrue("loadCreatedAdsMultipleAdsDifferentUsersTest: createAd() should return true with valid data", adService.createAd(name + "3", description + "3", price + 3, user2_id));

        List<Ad> createdAds = adService.loadCreatedAds(user.getId());
        assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: size of createdAds should be equal to 2", 2, createdAds.size());
        for (Ad ad : createdAds) {
            assertTrue("loadCreatedAdsMultipleAdsDifferentUsersTest: name of ad in list of all ads is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1"));
            if (ad.getName().equals(name)) {
                assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: second ad doesn't contain correct user", user, ad.getUser());
            }
        }
        // Ensure both created ads are still present in the DB
        List<Ad> allAds = adService.loadAllAds();
        assertEquals("loadCreatedAdsMultipleAdsDifferentUsersTest: size of allAds should be equal to 2", 4, allAds.size());
        for (Ad ad : allAds) {
            assertTrue("loadCreatedAdsMultipleAdsSameUserTest: name of ad in list of all ads is different than either of the names of the created ads", ad.getName().equals(name) | ad.getName().equals(name + "1") | ad.getName().equals(name + "2") | ad.getName().equals(name + "3"));
            if (ad.getName().equals(name)) {
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: first ad doesn't contain correct description", description, ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: first ad doesn't contain correct price", price, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: first ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "1")) {
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct description", description + "1", ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct price", price + 1, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct user", user, ad.getUser());
            } else if (ad.getName().equals(name + "2")) {
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct description", description + "2", ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct price", price + 2, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct user", user2, ad.getUser());
            } else if (ad.getName().equals(name + "3")) {
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct description", description + "3", ad.getDescription());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct price", price + 3, ad.getPrice());
                assertEquals("loadCreatedAdsMultipleAdsSameUserTest: second ad doesn't contain correct user", user2, ad.getUser());
            }
        }
    }

    // Ensure that loadCreatedAds() is unsuccessful when passed an invalid user ID within an empty DB
    @Test
    public void loadCreatedAdsInvalidId() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        // Using any user ID except for this single ID will be an invalid ID number
        int validId = user.getId();
        assertEquals("loadCreatedAdsInvalidId: shouldn't return any ads when passed an invalid ID", 0, adService.loadCreatedAds(validId + 1).size());
    }

    // Ensure that getReferenceById() returns the correct ad when passed a valid ID
    @Test
    public void getReferenceByIdValidTest() {
        User user = userService.findByUsernameIgnoreCase("Username1").get(0);
        int user_id = user.getId();

        assertTrue("getReferenceByIdValidTest: createAd() should return true with valid data", adService.createAd(name, description, price, user_id));
        List<Ad> allAds = adService.loadAllAds();
        assertEquals("getReferenceByIdValidTest: should only contain 1 ad in the DB", 1, allAds.size());
        int adId = adService.loadAllAds().get(0).getId();
        Ad newAd = adService.getReferenceById(adId);

        assertEquals("getReferenceByIdTest: name of ad should be equal to the name it was set to", newAd.getName(), name);
        assertEquals("getReferenceByIdTest: description of ad should be equal to the description it was set to", newAd.getDescription(), description);
        assertEquals("getReferenceByIdTest: price of ad should be equal to the price it was set to", newAd.getPrice(), price);
        assertEquals("getReferenceByIdTest: user of ad should be equal to the user it was set to", newAd.getUser().getId(), user_id);
    }

    // Ensure that getReferenceById returns fails when an invalid ID is passed
    @Test
    public void getReferenceByIdInvalidTest() {
        Ad invalidAd = adService.getReferenceById(1);

        assertEquals("getReferenceByIdTest: name should be null when passed an invalid ad ID", invalidAd.getName(), null);
        assertEquals("getReferenceByIdTest: name should be null when passed an invalid ad ID", invalidAd.getDescription(), null);
        assertEquals("getReferenceByIdTest: price should be null when passed an invalid ad ID", invalidAd.getPrice(), null);
        assertEquals("getReferenceByIdTest: user of ad should null", invalidAd.getUser(),null);
    }
}