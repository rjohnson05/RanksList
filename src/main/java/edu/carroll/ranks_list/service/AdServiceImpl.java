package edu.carroll.ranks_list.service;

import edu.carroll.ranks_list.model.Ad;
import edu.carroll.ranks_list.repository.AdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {
    private static final Logger log = LoggerFactory.getLogger(AdServiceImpl.class);
    private final AdRepository adRepo;
    private List<String> goalList = new ArrayList<String>();

    public AdServiceImpl(AdRepository adRepo) {
        this.adRepo = adRepo;
    }

    public void createAd(Ad newAd) {
        adRepo.save(newAd);
    }

    public List<Ad> loadAllAds() {
        return adRepo.findAll();
    }

    public List<Ad> loadSavedAds() {
        log.info("Entering loadSavedAds");
        List<Ad> savedAds = adRepo.findBySaved(true);
        return savedAds;
    }

    public Ad deleteAd(Integer id) {
        Ad deletedAd = adRepo.getReferenceById(id);
        adRepo.deleteById(id);
        log.info("Advertisement #{id} deleted");

        return deletedAd;
    }

    public Ad removeSavedAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setSaved(false);
        adRepo.save(changedAd);
        log.info("Advertisement #{id} removed from saved ads");

        return changedAd;
    }

    public Ad saveAd(Integer id) {
        Ad changedAd = adRepo.getReferenceById(id);
        changedAd.setSaved(true);
        adRepo.save(changedAd);

        return changedAd;
    }

    @Override
    public void addGoal(String s) {
        goalList.add(s);
    }
}