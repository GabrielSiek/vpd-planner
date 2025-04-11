package com.vpd.Movie;

import com.vpd.Tmdb.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/search")
    public String searchMovie(@RequestParam String query, @RequestParam(defaultValue = "1") String page) {
        try {
            return tmdbService.searchMovie(query, page);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}
