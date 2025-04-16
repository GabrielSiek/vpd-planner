package com.vpd.Movie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Movie.DTO.MovieDTO;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Movie.DTO.RegisterMovieDTO;
import com.vpd.Tmdb.TmdbService;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/movie")
public class MovieController {

    @Autowired
    private TmdbService tmdbService;

    @Autowired
    private MovieService movieService;

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
            @RequestBody RegisterMovieDTO movieDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = movieService.addMovie(movieDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<MovieDTO>> getMovie(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<MovieDTO> response = movieService.getMovie(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);

    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<ApiResponse<?>> deleteMovie(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = movieService.deleteMovie(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);

    }
    //A lógica para favoritar, dar nota e marcar como assistido vão ficar no usermoviecontroller
}
