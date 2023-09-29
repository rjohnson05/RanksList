package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;


import java.util.List;

public interface AdService {

    List<Ad> getAllAds();

    boolean createAd(String name, String description, Float price);

    List<Ad> loadAllAds();

    List<Ad> loadSavedAds();

    Ad deleteAd(Integer id);

    Ad removeSavedAd(Integer id);

    Ad saveAd(Integer id);


}

