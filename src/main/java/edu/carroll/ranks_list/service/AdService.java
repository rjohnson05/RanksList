package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;

import java.util.List;

public interface AdService {

    Ad newAd(Ad newAd);

    List<Ad> getAllAds();

    Ad deleteAds(Integer id);
}