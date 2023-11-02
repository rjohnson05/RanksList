package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;


import java.util.List;

public interface AdService {

    boolean createAd(String name, String description, Float price, Integer userId);

    boolean editAd(String name, String description, Float price, Integer userId);

    boolean starAd(Integer id);

    boolean removeStarredAd(Integer id);

    boolean deleteAd(Integer id);

    List<Ad> loadAllAds();

    List<Ad> loadStarredAds();

    List<Ad> loadCreatedAds(Integer id);

    Ad getReferenceById(Integer id);
}