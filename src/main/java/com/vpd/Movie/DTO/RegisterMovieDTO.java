package com.vpd.Movie.DTO;

import java.time.LocalDate;
import java.util.List;

public record RegisterMovieDTO(String travelId,
                               String originalTitle,
                               String originalLanguage,
                               String title,
                               String overview,
                               List<String> genres,
                               LocalDate releaseDate,
                               double voteAverage,
                               int voteCount,
                               double popularity,
                               String posterPath,
                               String backgroundPath,
                               boolean adult) {
}
