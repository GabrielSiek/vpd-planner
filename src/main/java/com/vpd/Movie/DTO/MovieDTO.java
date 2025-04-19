package com.vpd.Movie.DTO;

import com.vpd.Collection.DTO.SimpleCollectionDTO;

import java.time.LocalDate;
import java.util.List;

public record MovieDTO (String travelId,
                        String movieId,
                        String travelTitle,
                        List<SimpleCollectionDTO> collections,
                        Double stars,
                        boolean favorite,
                        boolean watched,
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
