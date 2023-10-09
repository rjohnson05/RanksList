package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import jakarta.transaction.TransactionScoped;
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
    final private String name = "testname";
    final private Float price = 5.99F;
    final private String description = "testdescription";
    final private Integer userId = 0;

    @Autowired
    private AdServiceImpl adService;

    @BeforeEach
    public void beforeTest() {
        assertNotNull("userService must be injected", adService);
    }
    
    @Test
    public void createAdValidDataTest() {
        // Ensure the dummy record is the only ad in the database
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        Ad createdAd = adService.loadAllAds().get(0);

        assertEquals("createAdValidDataTest: should pass with valid advertisement data", 1, adService.loadAllAds().size());
        assertEquals("createAdValidDataTest: should pass with valid advertisement data", name, createdAd.getName());
        assertEquals("createAdValidDataTest: should pass with valid advertisement data", price, createdAd.getPrice());
        assertEquals("createAdValidDataTest: should pass with valid advertisement data", description, createdAd.getDescription());
    }

    @Test
    public void createAdDuplicateAdTest() {
        // Ensure there are two identical ads in the database
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        adService.createAd(name, description, price, userId);

        assertEquals("createAdDuplicateTest: should pass with duplicate advertisements", 2, adService.loadAllAds().size());
    }

    @Test
    public void createAdPriceZeroTest() {
        // Ensure the dummy record is the only ad in the database
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, (float) 0, userId);

        assertEquals("createAdPriceZeroTest: should pass with a price of $0.00", 1, adService.loadAllAds().size());
    }

    @Test
    public void createAdNullNameTest() {
        // Ensure that an ad is NOT created if the user leaves the name field empty
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(null, description, price, userId);

        assertEquals("createAdNullNameTest: should fail with Null name", 0, adService.loadAllAds().size());
    }

    @Test
    public void createAdNullPriceTest() {
        // Ensure that an ad is NOT created if the user leaves the price field empty
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, null, userId);

        assertEquals("createAdNullPriceTest: should fail with Null price", 0, adService.loadAllAds().size());
    }

    @Test
    public void createAdNullDescriptionTest() {
        // Ensure that an ad is NOT created if the user leaves the description field empty
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, null, price, userId);

        assertEquals("createAdNullDescriptionTest: should fail with Null description", 0, adService.loadAllAds().size());
    }

    @Test
    public void createAdNullAllTest() {
        // Ensure that an ad is NOT created if the user leaves all fields empty
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, null, price, userId);

        assertEquals("createAdNullAllTest: should fail with all Null data", 0, adService.loadAllAds().size());
    }

    @Test
    public void loadAllAdsVariedNumberOfAdsTest() {
        // Ensure all ads are loaded despite the number of ads
        for (int i = 1; i < 10; i++) {
            adService.deleteAllAds();
            for (int j = 0; j < i; j++) {
                adService.createAd(name, description, price, userId);
            }
            assertEquals("loadAllAdsVariedNumberOfAdsTest: should pass with valid data for any number of ads", i, adService.loadAllAds().size());
        }
    }

    @Test
    public void loadAllAdsZerosAdsTest() {
        // Ensure that loading all ads from an empty database doesn't return any ads
        adService.deleteAllAds();
        assertEquals("loadAllAdsZerosAdsTest: should succeed when there are zeros ads", 0, adService.loadAllAds().size());
    }

    @Test
    public void loadAllAdsDuplicatesTest() {
        // Ensure that all ads with similar information are loaded
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        adService.createAd(name, description, price, userId);

        assertEquals("loadAllAdsDuplicatesTest: should successfully load each duplicate ad", 2, adService.loadAllAds().size());
    }

    @Test
    public void loadStarredAdsVariedNumberOfAdsTest() {
        // Ensure that all starred ads are loaded despite the number of ads present
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        for (int i = 1; i < 10; i++) {
            adService.removeAllStarredAds();
            for (int j = 0; j < i; j++) {
                adService.createAd(name, description, price, userId);
                int createdAdId = adService.loadAllAds().get(j).getId();
                adService.starAd(createdAdId);
            }
            assertEquals("loadStarredAdsVariedNumberOfAdsTest: should pass with varied number of starred ads", i, adService.loadStarredAds().size());
        }
    }

    @Test
    public void loadStarredAdsZeroAdsTest() {
        // Ensures that no ads are loaded when none are marked as starred
        if (!adService.loadStarredAds().isEmpty()) {
            adService.removeAllStarredAds();
        }
        assertEquals("loadStarredAdsZeroAdsTest: should pass with zero starred ads to load", 0, adService.loadStarredAds().size());
    }

    @Test
    public void loadStarredAdsNotAllStarredTest() {
        // Ensures that only the starred ads are loaded when there are unstarred ads in the database
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        for (int i = 0; i < 10; i++) {
            adService.createAd(name, description, price, userId);
            if (i < 5) {
                Ad createdAd = adService.loadAllAds().get(i);
                adService.starAd(createdAd.getId());
            }
        }
        assertEquals("loadStarredAdsNotAllStarredTest: should pass with several unstarred ads present", 5, adService.loadStarredAds().size());
    }

    @Test
    public void deleteAdValidIdTest() {
        // Ensures that the designated ad is deleted when a legitimate ID is given
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        adService.createAd(name, description, price, userId);
        Integer createdAdId = adService.loadAllAds().get(0).getId();
        adService.deleteAd(createdAdId);

        assertEquals("deleteAdValidIdTest: should pass when ID is present within the database", 1, adService.loadAllAds().size());
        assertNotEquals("deleteAdValidIdTest: should pass if none of the remaining ads have the deleted ID", createdAdId, adService.loadAllAds().get(0).getId());
    }

    @Test
    public void deleteAdInvalidIdTest() {
        // Ensure that an ad with a different ID is not deleted
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        int createdAdId = adService.loadAllAds().get(0).getId();
        adService.deleteAd(createdAdId - 1);

        assertEquals("deleteAdInvalidIdTest: ad should not be deleted if not correct ID", 1, adService.loadAllAds().size());
    }

    @Test
    public void deleteAllAdsVariedNumberOfAdsTest() {
        // Ensures that all ads in the database are deleted, no matter the number of ads present
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                adService.createAd(name, description, price, userId);
            }
            adService.deleteAllAds();
            assertEquals("deleteAllAdsVariedNumberOfAdsTest: should pass if all ads are deleted", 0, adService.loadAllAds().size());
        }
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
        adService.deleteAllAds();

        assertEquals("deleteAllAdsZeroAdsTest: should pass if no ads are present to delete", 0, adService.loadAllAds().size());
    }

    @Test
    public void removeStarredAdValidIdTest() {
        // Ensures that the designated ad is removed from the list of starred ads
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        adService.createAd(name, description, price, userId);
        int createdAdId = adService.loadAllAds().get(1).getId();
        adService.starAd(createdAdId);
        adService.removeStarredAd(createdAdId);

        assertEquals("removeStarredAdValidIdTest: should pass if ad is kept on the list of all ads", 2, adService.loadAllAds().size());
        assertEquals("removeStarredAdValidIdTest: should pass if ad is removed from starred list", 0, adService.loadStarredAds().size());
    }

//    @Test
//    public void removeStarredAdInvalidIdTest() {
//        // Ensures that no ad is unstarred if the designated ID is not valid
//        if (!adService.loadAllAds().isEmpty()) {
//            adService.deleteAllAds();
//        }
//        adService.createAd(name, description, price);
//        adService.createAd(name, description, price);
//        int createdAdId = adService.loadAllAds().get(1).getId();
//        adService.starAd(createdAdId);
//        adService.removeStarredAd(createdAdId + 1);
//
//        assertEquals("removeStarredAdInvalidIdTest: should pass if two ads were created", 2, adService.loadAllAds().size());
//        assertEquals("removeStarredAdInvalidIdTest: should pass if the starred ad was not removed", 1, adService.loadStarredAds().size());
//    }

    @Test
    public void removeAllStarredAdsVariedNumberOfAdsTest() {
        // Ensures that all starred ads are unstarred, no matter the number of starred ads present
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < i; j++) {
                adService.createAd(name, description, price, userId);
                int adId = adService.loadAllAds().get(adService.loadAllAds().size() - 1).getId();
                adService.starAd(adId);
            }
        }
        adService.removeAllStarredAds();

        assertEquals("removeAllStarredAdsVariedNumberOfAdsTest: should pass if all starred ads were removed", 0, adService.loadStarredAds().size());
    }

    @Test
    public void removeAllStarredAdsNoAdsTest() {
        // Ensures that nothing is done if there are no starred ads
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        adService.removeAllStarredAds();

        assertEquals("removeAllStarredAdsNoAdsTest: should pass if there are no starred ads", 0, adService.loadStarredAds().size());
    }

    @Test
    public void starAdValidIdTest() {
        // Ensures that starring an ad adds it to the list of starred ads
        if (!adService.loadAllAds().isEmpty()) {
            adService.deleteAllAds();
        }
        adService.createAd(name, description, price, userId);
        int adId = adService.loadAllAds().get(0).getId();
        adService.starAd(adId);

        assertEquals("starAdValidIdTest: should pass if the designated ad is added to the list of starred ads", 1, adService.loadStarredAds().size());
        assertEquals("starAdValidIdTest: should pass if the starred ad still appears in the list of all ads", 1, adService.loadAllAds().size());
    }

//    @Test
//    public void starAdInvalidIdTest() {
//        // Ensures that no ad is starred if an invalid ID is passed
//        if (!adService.loadAllAds().isEmpty()) {
//            adService.deleteAllAds();
//        }
//        adService.createAd(name, description, price);
//        int adId = adService.loadAllAds().get(0).getId();
//        adService.starAd(adId + 1);
//
//        assertEquals("starAdInvalidIdTest: should pass if no ads are added to the starred list", 0, adService.loadStarredAds().size());
//        assertEquals("starAdInvalidIdTest: should pass if all ads are still in the list of all ads", 1, adService.loadAllAds().size());
//    }
}