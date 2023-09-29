package edu.carroll.ranks_list.controller;


import edu.carroll.ranks_list.form.AdForm;
import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.service.AdService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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
    boolean newAd(@RequestBody AdForm adForm) {
        boolean newAd = adService.newAd(adForm.getName(), adForm.getDescription(), adForm.getPrice());
        return newAd;
    }

    @GetMapping("/ads")
    List<Ad> getAllAds() {
        return adService.getAllAds();
    }

    @DeleteMapping("/ads/{id}")
    public Ad deleteAd(@PathVariable("id") Integer id) {
        log.info("Advertisement #{id} deleted");
        return adService.deleteAds(id);
    }
}
