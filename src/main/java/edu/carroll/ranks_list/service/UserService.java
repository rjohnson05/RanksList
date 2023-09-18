package edu.carroll.ranks_list.service;

public interface UserService {
    /**
     * Given a loginForm, determine if the information provided is valid, and the user exists in the system.
     * @return true if data exists and matches what's on record, false otherwise
     */


    boolean validateUser(String username, String password);



}