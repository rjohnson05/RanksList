package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.model.User;


import java.util.List;

public interface AdService {

    boolean createAd(String name, String description, Float price, User user);

    boolean editAd(String name, String description, Float price, Integer id);

    boolean starAd(Integer id);

    boolean removeStarredAd(Integer id);

    boolean deleteAd(Integer id);

    List<Ad> loadAllAds();

    List<Ad> loadStarredAds();

    List<Ad> loadCreatedAds(Integer id);

    Ad getReferenceById(Integer id);
}