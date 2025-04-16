package com.vpd.UserMovie;

import com.vpd.Movie.Movie;
import com.vpd.User.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_movie")
public class UserMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Double stars;

    private boolean favorite;

    private boolean watched;

    public UserMovie() {
        this.favorite = false;
        this.watched = false;
        this.stars = null;
    }
}
