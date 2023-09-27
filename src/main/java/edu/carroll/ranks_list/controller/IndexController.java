package edu.carroll.ranks_list.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * API for HTTP requests regarding the home page for the app.
 *
 * @author Hank Rugg, Ryan Johnson
 */
@Controller
public class IndexController {
    /**
     * Constructor for the Index Controller.
     *
     * @return String object detailing the name of the .html file containing the home page
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}