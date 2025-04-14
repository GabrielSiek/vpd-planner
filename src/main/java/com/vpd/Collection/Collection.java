package com.vpd.Collection;

import com.vpd.Image.Image;
import com.vpd.Image.ImageService;
import com.vpd.Movie.Movie;
import com.vpd.Travel.Travel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "collections")
public class Collection {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private Image poster;

    @ManyToMany
    @JoinTable(
            name = "collection_movie",
            joinColumns = @JoinColumn(name = "collection_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> movies;
    //quando criar tem q criar como um hashset

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

}
