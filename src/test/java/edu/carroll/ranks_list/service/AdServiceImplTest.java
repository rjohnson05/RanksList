package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.springframework.test.util.AssertionErrors.*;

@SpringBootTest
@Transactional
public class AdServiceImplTest {
    private final String name = "testname";
    private final Float price = 6.99F;
    private final String description = "testdescription";
    private final User user = new User("username", "password");

    @Autowired
    private AdService adService;


    // Tests to ensure createAd() method creates and stores a single advertisement correctly when passed valid data
    @Test
    public void createAdValidDataTest() {
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
        adService.createAd(name, description, price, user);
        User newUser = new User("username1", "password");
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
        adService.createAd(name, description, price, user);
        List<Ad> allCreatedAds = adService.loadAllAds();
        assertEquals("createAdDuplicateAdDifferentUserIdTest: should create a single ad with valid data", 1, allCreatedAds.size());

        User newUser = new User("username1", "password");
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
        adService.createAd(name, description, price, user);
        assertFalse("createAdDuplicateAdSameUserIdTest: attempted duplicate ad should fail when having the same user ID", adService.createAd(name, description, price, user));
    }

    // Tests to ensure that createAd() will fail if no name is provided
    @Test
    public void createAdNoNameTest() {
        assertFalse("createAdNoNameTest: should not create successfully when no name is supplied", adService.createAd("", description, price, user));
        assertEquals("createAdNoNameTest: should not create an ad when no name is supplied", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() will create an ad even if no description is provided
    @Test
    public void createAdNoDescriptionTest() {
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
        assertFalse("createAdNullNameTest: should return false when passed null name", adService.createAd(null, description, price, user));
        assertEquals("createAdNullNameTest: should fail with null name", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when passed a null price
    @Test
    public void createAdNullPriceTest() {
        assertFalse("createAdNullPriceTest: should return false when passed null price", adService.createAd(name, description, null, user));
        assertEquals("createAdNullPriceTest: should fail with null price", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when passed a null price
    @Test
    public void createAdNullDescriptionTest() {
        assertFalse("createAdNullDescriptionTest: should return false when passed null description", adService.createAd(name, null, price, user));
        assertEquals("createAdNullDescriptionTest: should fail with null description", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that createAd() doesn't create an ad when all parameters are null
    @Test
    public void createAdNullAllTest() {
        assertFalse("createAdNullAllTest: should return false when passed all null values", adService.createAd(null, null, null, null));
        assertEquals("createAdNullAllTest: should fail with all Null data", 0, adService.loadAllAds().size());
    }

    // Tests to ensure that editAd() correctly changes all the data of an advertisement if given valid data
    @Test
    public void editAdValidDataAllChangeTest() {
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when valid data is given", adService.editAd(name + "1", description + "1", price + 1, adService.loadAllAds().get(0).getId()));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name + "1", editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price + 1, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description + "1", editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes the name of an advertisement if given valid data
    @Test
    public void editAdValidDataNameChangeTest() {
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name + "1", description, price, adService.loadAllAds().get(0).getId()));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name + "1", editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description, editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes the price of an advertisement if given valid data
    @Test
    public void editAdValidDataPriceChangeTest() {
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name, description, price + 1, adService.loadAllAds().get(0).getId()));
        assertEquals("editAdValidDataTest: number of ads not the same as before editing ad", 1, adService.loadAllAds().size());
        Ad editedAd = adService.loadAllAds().get(0);
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", name, editedAd.getName());
        assertEquals("editAdValidDataTest: price didn't change to proper value when edited", price + 1, editedAd.getPrice());
        assertEquals("editAdValidDataTest: description didn't change to proper value when edited", description, editedAd.getDescription());
        assertEquals("editAdValidDataTest: name didn't change to proper value when edited", user, editedAd.getUser());
    }

    // Tests to ensure that editAd() correctly changes the data of an advertisement if given valid data
    @Test
    public void editAdValidDataDescriptionChangeTest() {
        adService.createAd(name, description, price, user);
        assertTrue("editAdValidDataTest: should return true when name is changed to valid name", adService.editAd(name, description + "1", price, adService.loadAllAds().get(0).getId()));
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
        adService.createAd(name, description, price, user);
        assertFalse("editAdEmptyNameTest: shouldn't be successful when name is changed to be empty", adService.editAd("", description, price, adService.loadAllAds().get(0).getId()));
        assertEquals("editAdEmptyNameTest: number of ads isn't equal to before an ad was edited", 1, adService.loadAllAds().size());
        Ad createdAd = adService.loadAllAds().get(0);
        assertEquals("editAdEmptyNameTest: name of ad isn't same as before unsuccessful editing", name, createdAd.getName());
        assertEquals("editAdEmptyNameTest: price of ad isn't same as before unsuccessful editing", price, createdAd.getPrice());
        assertEquals("editAdEmptyNameTest: description of ad isn't same as before unsuccessful editing", description, createdAd.getDescription());
        assertEquals("editAdEmptyNameTest: user of ad isn't same as before unsuccessful editing", user, createdAd.getUser());
    }

//    @Test
//    public void loadAllAdsVariedNumberOfAdsTest() {
//        // Ensure all ads are loaded despite the number of ads
//        for (int i = 1; i < 10; i++) {
//            List<Ad> allAds = adService.loadAllAds();
//            for (Ad allAd : allAds) {
//                adService.deleteAd(allAd.getId());
//            }
//            for (int j = 0; j < i; j++) {
//                adService.createAd(name, description, price, user);
//            }
//            assertEquals("loadAllAdsVariedNumberOfAdsTest: should pass with valid data for any number of ads", i, adService.loadAllAds().size());
//        }
//    }

    @Test
    public void loadAllAdsZerosAdsTest() {
        assertEquals("loadAllAdsZerosAdsTest: should succeed when there are zeros ads", 0, adService.loadAllAds().size());
    }

//    @Test
//    public void loadAllAdsDuplicatesTest() {
//        adService.createAd(name, description, price, user);
//        adService.createAd(name, description, price, user);
//
//        assertEquals("loadAllAdsDuplicatesTest: should successfully load each duplicate ad", 2, adService.loadAllAds().size());
//    }

//    @Test
//    public void loadStarredAdsVariedNumberOfAdsTest() {
//        for (int i = 1; i < 10; i++) {
//            List<Ad> starredAds = adService.loadStarredAds();
//            for (Ad allAd : starredAds) {
//                adService.deleteAd(allAd.getId());
//            }
//            for (int j = 0; j < i; j++) {
//                adService.createAd(name, description, price, user);
//                int createdAdId = adService.loadAllAds().get(j).getId();
//                adService.starAd(createdAdId);
//            }
//            assertEquals("loadStarredAdsVariedNumberOfAdsTest: should pass with varied number of starred ads", i, adService.loadStarredAds().size());
//        }
//    }

    @Test
    public void loadStarredAdsZeroAdsTest() {
        assertEquals("loadStarredAdsZeroAdsTest: should pass with zero starred ads to load", 0, adService.loadStarredAds().size());
    }

//    @Test
//    public void loadStarredAdsNotAllStarredTest() {
//        for (int i = 0; i < 10; i++) {
//            adService.createAd(name, description, price, user);
//            if (i < 5) {
//                Ad createdAd = adService.loadAllAds().get(i);
//                adService.starAd(createdAd.getId());
//            }
//        }
//        assertEquals("loadStarredAdsNotAllStarredTest: should pass with several unstarred ads present", 5, adService.loadStarredAds().size());
//    }

//    @Test
//    public void deleteAdValidIdTest() {
//        adService.createAd(name, description, price, user);
//        adService.createAd(name, description, price, user);
//        Integer createdAdId = adService.loadAllAds().get(0).getId();
//        adService.deleteAd(createdAdId);
//
//        assertEquals("deleteAdValidIdTest: should pass when ID is present within the database", 1, adService.loadAllAds().size());
//        assertNotEquals("deleteAdValidIdTest: should pass if none of the remaining ads have the deleted ID", createdAdId, adService.loadAllAds().get(0).getId());
//    }

    @Test
    public void deleteAdInvalidIdTest() {
        adService.createAd(name, description, price, user);
        int createdAdId = adService.loadAllAds().get(0).getId();
        adService.deleteAd(createdAdId - 1);

        assertEquals("deleteAdInvalidIdTest: ad should not be deleted if not correct ID", 1, adService.loadAllAds().size());
    }

//    @Test
//    public void deleteAllAdsVariedNumberOfAdsTest() {
//        // Ensures that all ads in the database are deleted, no matter the number of ads present
//        for (int i = 0; i < 10; i++) {
//            for (int j = 0; j < i; j++) {
//                adService.createAd(name, description, price, user);
//            }
//            List<Ad> allAds = adService.loadAllAds();
//            for (Ad allAd : allAds) {
//                adService.deleteAd(allAd.getId());
//            }
//            assertEquals("deleteAllAdsVariedNumberOfAdsTest: should pass if all ads are deleted", 0, adService.loadAllAds().size());
//        }
//    }

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

//    @Test
//    public void removeStarredAdValidIdTest() {
//        adService.createAd(name, description, price, user);
//        adService.createAd(name, description, price, user);
//        int createdAdId = adService.loadAllAds().get(1).getId();
//        adService.starAd(createdAdId);
//        adService.removeStarredAd(createdAdId);
//
//        assertEquals("removeStarredAdValidIdTest: should pass if ad is kept on the list of all ads", 2, adService.loadAllAds().size());
//        assertEquals("removeStarredAdValidIdTest: should pass if ad is removed from starred list", 0, adService.loadStarredAds().size());
//    }

//    @Test
//    public void removeStarredAdInvalidIdTest() {
//        // Ensures that no ad is unstarred if the designated ID is not valid
//        if (!adService.loadAllAds().isEmpty()) {
//            List<Ad> allAds = adService.loadAllAds();
//            for (Ad allAd : allAds) {
//                adService.deleteAd(allAd.getId());
//            }
//        }
//        adService.createAd(name, description, price, user);
//        adService.createAd(name, description, price, user);
//        int createdAdId = adService.loadAllAds().get(1).getId();
//        adService.starAd(createdAdId);
//        adService.removeStarredAd(createdAdId + 1);
//
//        assertEquals("removeStarredAdInvalidIdTest: should pass if two ads were created", 2, adService.loadAllAds().size());
//        assertEquals("removeStarredAdInvalidIdTest: should pass if the starred ad was not removed", 1, adService.loadStarredAds().size());
//    }

    @Test
    public void removeAllStarredAdsVariedNumberOfAdsTest() {
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

    @Test
    public void removeAllStarredAdsNoAdsTest() {
        adService.createAd(name, description, price, user);
        List<Ad> starredAds = adService.loadStarredAds();
        for (Ad allAd : starredAds) {
            adService.deleteAd(allAd.getId());
        }

        assertEquals("removeAllStarredAdsNoAdsTest: should pass if there are no starred ads", 0, adService.loadStarredAds().size());
    }

    @Test
    public void starAdValidIdTest() {
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        adService.starAd(adId);

        assertEquals("starAdValidIdTest: should pass if the designated ad is added to the list of starred ads", 1, adService.loadStarredAds().size());
        assertEquals("starAdValidIdTest: should pass if the starred ad still appears in the list of all ads", 1, adService.loadAllAds().size());
    }

//    @Test
//    public void starAdInvalidIdTest() {
//        // Ensures that no ad is starred if an invalid ID is passed
//        if (!adService.loadAllAds().isEmpty()) {
//            List<Ad> allAds = adService.loadAllAds();
//            for (Ad allAd : allAds) {
//                adService.deleteAd(allAd.getId());
//            }
//        }
//        adService.createAd(name, description, price, user);
//        int adId = adService.loadAllAds().get(0).getId();
//        adService.starAd(adId + 1);
//
//        assertEquals("starAdInvalidIdTest: should pass if no ads are added to the starred list", 0, adService.loadStarredAds().size());
//        assertEquals("starAdInvalidIdTest: should pass if all ads are still in the list of all ads", 1, adService.loadAllAds().size());
//    }
}