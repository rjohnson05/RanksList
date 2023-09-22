package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class AdController {
    private static final Logger log = LoggerFactory.getLogger(AdController.class);
    private final AdRepository adRepo;

    public AdController(AdRepository adRepo) {
        this.adRepo = adRepo;
    }

    @PostMapping("/ads")
    Ad newAd(@RequestBody Ad newAd) {
        return adRepo.save(newAd);
    }

    @GetMapping("/ads")
    List<Ad> getAllAds() {
        return adRepo.findAll();
    }

    @DeleteMapping("/ads/{id}")
    public Ad deleteAd(@PathVariable("id") Integer id) {
        Ad deletedAd = adRepo.getReferenceById(id);
        adRepo.deleteById(id);
        log.info("Advertisement #{id} deleted");

        return deletedAd;
    }
}
