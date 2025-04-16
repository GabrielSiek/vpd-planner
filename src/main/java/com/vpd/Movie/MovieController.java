package com.vpd.Movie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Tmdb.TmdbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/movies")
public class MovieController {

    @Autowired
    private TmdbService tmdbService;

    @GetMapping("/search")
    public String searchMovie(
            @RequestParam String query,
            @RequestParam(defaultValue = "1") String page) {
        try {
            return tmdbService.searchMovie(query, page);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }

    //create e delete precisa atualizar o usermovierepository tbm
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<?>> addMovie(
            @RequestBody MovieIdDTO movieIdDTO) {

        ApiResponse<?> response = tmdbService.getMovieById(movieIdDTO.movieId());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    //A lógica para favoritar, dar nota e marcar como assistido vão ficar no usermoviecontroller
}
