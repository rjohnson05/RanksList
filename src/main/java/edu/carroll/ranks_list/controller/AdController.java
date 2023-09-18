package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class AdController {
    private static final Logger log = LoggerFactory.getLogger(AdController.class);

    @Autowired
    private AdRepository adRepository;

    @PostMapping("/ads")
    Ad newAd(@RequestBody Ad newAd) {
        System.out.println("Posted ad");
        return adRepository.save(newAd);
    }

    @GetMapping("/ads")
    List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    @DeleteMapping("/ads/{id}")
    public void deleteAd(@PathVariable("id") Integer id) {
        System.out.println("Delete Mapping");
        // Ad deletedAd = adRepository.getReferenceById(id);
        adRepository.deleteById(id);
        System.out.println(id);
        log.info("Advertisement #{id} deleted");

        // return deletedAd;
    }
}
