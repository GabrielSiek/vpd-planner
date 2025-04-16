package com.vpd.Movie.DTO;

import com.vpd.UserMovie.DTO.UserMovieDTO;

import java.util.List;

public record MainPageMovieDTO(String id,
                               String title,
                               String posterPath,
                               List<String> genres,
                               Double stars,
                               boolean favorite,
                               boolean watched) {
}
