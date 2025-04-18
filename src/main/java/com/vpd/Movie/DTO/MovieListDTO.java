package com.vpd.Movie.DTO;

import java.util.List;

public record MovieListDTO(List<MainPageMovieDTO> movies,
                           int totalMovies) {
}
