package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;


import java.util.List;

public interface AdService {


    List<Ad> getAllAds();

    boolean createAd(String name, String description, Float price, Integer userId);

    List<Ad> loadAllAds();

    List<Ad> loadStarredAds();

    Ad deleteAd(Integer id);

    Ad removeStarredAd(Integer id);

    Ad starAd(Integer id);

    List<Ad> loadCreatedAds(Integer request);

    Ad getReferenceById(Integer id);

    boolean editAd(String name, String description, Float price, Integer userId);
}