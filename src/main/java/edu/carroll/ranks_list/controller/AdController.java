package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import edu.carroll.ranks_list.service.AdService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class AdController {
    private static final Logger log = LoggerFactory.getLogger(AdController.class);
    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping("/ads")
    Ad newAd(@RequestBody Ad newAd) {
        adService.createAd(newAd);
        return newAd;
    }

    @GetMapping("/ads")
    List<Ad> getAllAds() {
        return adService.loadAllAds();
    }

    @DeleteMapping("/ads/{id}")
    public Ad deleteAd(@PathVariable("id") Integer id) {
        return adService.deleteAd(id);
    }

    @GetMapping("/saved_ads")
    List<Ad> getSavedAds() {
        return adService.loadSavedAds();
    }

    @PutMapping("/saved_ads/{id}")
    Ad changeAdStatus(@PathVariable("id") Integer id) {
        Ad savedAd = adService.saveAd(id);
        log.info("Saved Ad: " + savedAd);
        return savedAd;
    }

    @DeleteMapping("/saved_ads/{id}")
    Ad removedSavedAd(@PathVariable("id") Integer id) {
        return adService.removeSavedAd(id);
    }
}