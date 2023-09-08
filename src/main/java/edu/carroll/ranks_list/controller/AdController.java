package edu.carroll.ranks_list.controller;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AdController {
    @Autowired
    private AdRepository adRepository;

    @PostMapping("/ad")
    Ad newAd(@RequestBody Ad newAd) {
        return adRepository.save(newAd);
    }

    @GetMapping("/ads")
    List<Ad> getAllAds() {
        return adRepository.findAll();
    }
}
