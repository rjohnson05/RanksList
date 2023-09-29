package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;

import java.util.List;

public interface AdService {

    List<Ad> getAllAds();

    Ad deleteAds(Integer id);

    boolean newAd(String name, String description, Float price);
}