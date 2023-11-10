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


    @Test
    public void deleteAdInvalidIdTest() {
        adService.createAd(name, description, price, user);
        int createdAdId = adService.loadAllAds().get(0).getId();
        adService.deleteAd(createdAdId - 1);

        assertEquals("deleteAdInvalidIdTest: ad should not be deleted if not correct ID", 1, adService.loadAllAds().size());
    }

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


    @Test
    public void loadAllAdsZerosAdsTest() {
        assertEquals("loadAllAdsZerosAdsTest: should succeed when there are zeros ads", 0, adService.loadAllAds().size());
    }

    @Test
    public void loadAllAdsTest() {
        adService.createAd(name, description, price, user);
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> allAds = adService.loadAllAds();

        assertEquals("loadAllAdsTest: id of ad should be equal to the id it was set to", allAds.get(0).getUser().getId(), userId);
        assertEquals("loadAllAdsTest: name of ad should be equal to the name it was set to", allAds.get(0).getName(), name);
        assertEquals("loadAllAdsTest: description of ad should be equal to the description it was set to", allAds.get(0).getDescription(), description);
        assertEquals("loadAllAdsTest: price of ad should be equal to the price it was set to", allAds.get(0).getPrice(), price);
        assertEquals("loadAllAdsTest: user of ad should be equal to the user it was set to", allAds.get(0).getUser(), user);

    }

    @Test
    public void loadAllAdsInvalidTest() {
        adService.createAd(name, description, price, user);
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> allAds = adService.loadAllAds();

        assertFalse("loadAllAdsInvalidTest: id plus one of ad should not be equal to the id it was set to", allAds.get(0).getUser().getId() == userId+1);
        assertFalse("loadAllAdsInvalidTest: a different name of ad should not be equal to the name it was set to", allAds.get(0).getName().equals("ThisIsNotTheName"));
        assertFalse("loadAllAdsInvalidTest: a different description of ad should not be equal to the description it was set to", allAds.get(0).getDescription().equals("ThisIsNotTheDescription"));
        assertFalse("loadAllAdsInvalidTest: price plus one of ad should be equal to the price it was set to", allAds.get(0).getPrice() == price+1);
        assertEquals("loadAllAdsInvalidTest: user of ad should be equal to the user it was set to", allAds.get(0).getUser(), user);

    }

    @Test
    public void loadAllAdsMultipleAdsTest() {
        int count = 0;
        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user);
            count++;
        }
        List<Ad> allAds = adService.loadAllAds();
        assertEquals("loadAllAdsMultipleAdsTest: size of allAds should be equal to the amount of ads that were added", count, allAds.size());
    }

    @Test
    public void loadAllAdsMultipleAdsDifferentSizeTest() {
        int count = 0;
        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user);
            count++;
        }
        count++;
        List<Ad> allAds = adService.loadAllAds();
        assertFalse("loadAllAdsMultipleAdsDifferentSizeTest: size of allAds should not be equal to the amount of ads plus 1 that were added", count == allAds.size());
    }


    @Test
    public void loadAllAdsDifferentUserCreatedTest() {
        User user2 = new User("Username2", "Password2");
        User user3 = new User("Username3", "Password3");

        int count = 0;


        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user2);
            count++;
        }

        for (int i = 0; i < 5; i++){
            adService.createAd(name, description, price+i, user3);
            count++;
        }

        int user2Id = adService.loadAllAds().get(0).getUser().getId();
        int user3Id = adService.loadAllAds().get(12).getUser().getId();

        List<Ad> allAds = adService.loadAllAds();

        assertEquals("loadCreatedAdsDifferentUserCreatedTest: size of allAds should be 15", count, allAds.size());
        assertFalse("loadCreatedAdsDifferentUserCreatedTest: size of allAds should not be 5", 5 ==  allAds.size());

    }

    @Test
    public void loadCreatedAdsTest() {
        adService.createAd(name, description, price, user);
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> createdAds = adService.loadCreatedAds(userId);

        assertEquals("loadCreatedAdsTest: id of ad should be equal to the id it was set to", createdAds.get(0).getUser().getId(), userId);
        assertEquals("loadCreatedAdsTest: name of ad should be equal to the name it was set to", createdAds.get(0).getName(), name);
        assertEquals("loadCreatedAdsTest: description of ad should be equal to the description it was set to", createdAds.get(0).getDescription(), description);
        assertEquals("loadCreatedAdsTest: price of ad should be equal to the price it was set to", createdAds.get(0).getPrice(), price);
        assertEquals("loadCreatedAdsTest: user of ad should be equal to the user it was set to", createdAds.get(0).getUser(), user);

    }

    @Test
    public void loadCreatedAdsInvalidTest() {
        adService.createAd(name, description, price, user);
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> createdAds = adService.loadCreatedAds(userId);

        assertFalse("loadCreatedAdsInvalidTest: id plus one of ad should not be equal to the id it was set to", createdAds.get(0).getUser().getId() == userId+1);
        assertFalse("loadCreatedAdsInvalidTest: a different name of ad should not be equal to the name it was set to", createdAds.get(0).getName().equals("ThisIsNotTheName"));
        assertFalse("loadCreatedAdsInvalidTest: a different description of ad should not be equal to the description it was set to", createdAds.get(0).getDescription().equals("ThisIsNotTheDescription"));
        assertFalse("loadCreatedAdsInvalidTest: price plus one of ad should be equal to the price it was set to", createdAds.get(0).getPrice() == price+1);
        assertEquals("loadCreatedAdsInvalidTest: user of ad should be equal to the user it was set to", createdAds.get(0).getUser(), user);

    }

    @Test
    public void loadCreatedAdsMultipleAdsTest() {
        int count = 0;
        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user);
            count++;
        }
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> createdAds = adService.loadCreatedAds(userId);
        assertEquals("loadCreatedAdsMultipleAdsTest: size of createdAds should be equal to the amount of ads that were added", count, createdAds.size());
    }

    @Test
    public void loadCreatedAdsMultipleAdsDifferentSizeTest() {
        int count = 0;
        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user);
            count++;
        }
        count++;
        int userId = adService.loadAllAds().get(0).getUser().getId();
        List<Ad> createdAds = adService.loadCreatedAds(userId);
        assertFalse("loadCreatedAdsMultipleAdsDifferentSizeTest: size of createdAds should not be equal to the amount of ads plus 1 that were added", count == createdAds.size());
    }


    @Test
    public void loadCreatedAdsDifferentUserCreatedTest() {
        User user2 = new User("Username2", "Password2");
        User user3 = new User("Username3", "Password3");

        int countForUser2 = 0;
        int countForUser3 = 0;


        for (int i = 0; i < 10; i++){
            adService.createAd(name, description, price+i, user2);
            countForUser2++;
        }

        for (int i = 0; i < 5; i++){
            adService.createAd(name, description, price+i, user3);
            countForUser3++;
        }

        int user2Id = adService.loadAllAds().get(0).getUser().getId();
        int user3Id = adService.loadAllAds().get(12).getUser().getId();

        List<Ad> user2createdAds = adService.loadCreatedAds(user2Id);
        List<Ad> user3createdAds = adService.loadCreatedAds(user3Id);

        assertEquals("loadCreatedAdsDifferentUserCreatedTest: size of user2createdAds should be 10", countForUser2, user2createdAds.size());
        assertEquals("loadCreatedAdsDifferentUserCreatedTest: size of user3createdAds should be 5", countForUser3, user3createdAds.size());
        assertFalse("loadCreatedAdsDifferentUserCreatedTest: size of user2createdAds should not be 5", 5 ==  user2createdAds.size());
        assertFalse("loadCreatedAdsDifferentUserCreatedTest: size of user3createdAds should not be 10", 10 ==  user3createdAds.size());

    }


    @Test
    public void getReferenceByIdValidTest() {
        adService.createAd(name, description, price, user);
        int adId = adService.loadAllAds().get(0).getId();
        Ad newAd = adService.getReferenceById(adId);

        assertEquals("getReferenceByIdTest: id of ad should be equal to the id it was set to", newAd.getId(), adId);
        assertEquals("getReferenceByIdTest: name of ad should be equal to the name it was set to", newAd.getName(), name);
        assertEquals("getReferenceByIdTest: description of ad should be equal to the description it was set to", newAd.getDescription(), description);
        assertEquals("getReferenceByIdTest: price of ad should be equal to the price it was set to", newAd.getPrice(), price);
        assertEquals("getReferenceByIdTest: user of ad should be equal to the user it was set to", newAd.getUser(), user);
    }

    @Test
    public void getReferenceByIdInvalidTest() {
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