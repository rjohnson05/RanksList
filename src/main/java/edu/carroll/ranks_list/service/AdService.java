package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;


import java.util.List;

public interface AdService {

    boolean createAd(String name, String description, Float price);

    List<Ad> loadAllAds();

    List<Ad> loadStarredAds();

    Ad deleteAd(Integer id);

    Ad removeStarredAd(Integer id);

    Ad starAd(Integer id);
}

