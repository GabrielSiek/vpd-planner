package com.vpd.Movie.DTO;

import java.util.List;

public record SearchMovieDTO(String search,
                             List<String> genres,
                             int page,
                             int size) {
}
