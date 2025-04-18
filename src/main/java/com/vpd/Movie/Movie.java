package com.vpd.Movie;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vpd.Collection.Collection;
import com.vpd.Travel.Travel;
import com.vpd.UserMovie.UserMovie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies")
public class Movie {

    //link poster
    //https://image.tmdb.org/t/p/original/

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String originalTitle;

    private String originalLanguage;

    private String title;

    @Column(length = 500)
    private String overview;

    private List<String> genres;

    private LocalDate releaseDate;

    private double voteAverage;

    private int voteCount;

    private double popularity;

    private String posterPath;

    private String backgroundPath;

    private boolean adult;

    @ManyToMany(mappedBy = "movies")
    @JsonIgnore
    private Set<Collection> collections;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @OneToMany(mappedBy = "movie")
    private List<UserMovie> userMovies;
}
