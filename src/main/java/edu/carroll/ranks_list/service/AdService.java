package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;

import java.util.List;

public interface AdService {
    void createAd(Ad newAd);
    List<Ad> loadAllAds();
    List<Ad> loadSavedAds();
    Ad deleteAd(Integer id);
    Ad removeSavedAd(Integer id);
    Ad saveAd(Integer id);
    void addGoal(String s);
}
