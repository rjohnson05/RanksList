package edu.carroll.ranks_list.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdServiceImpl implements AdService {

    private List<String> goalList = new ArrayList<String>();

    @Override
    public void addGoal(String s) {
        goalList.add(s);
    }



}
