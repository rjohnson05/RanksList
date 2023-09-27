package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    private AdRepository adRepo;
    public AdServiceImpl(AdRepository adRepo){
        this.adRepo = adRepo;
    }


    @Override
    public Ad newAd(Ad newAd) {
        adRepo.save(newAd);
        return newAd;
    }

    @Override
    public List<Ad> getAllAds() {
        return adRepo.findAll();
    }

    @Override
    public Ad deleteAds(Integer id) {
        Ad deletedAd = adRepo.getReferenceById(id);
        adRepo.deleteById(id);
        return deletedAd;
    }

}
