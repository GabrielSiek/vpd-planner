package com.vpd.UserMovie;

import com.vpd.Movie.Movie;
import com.vpd.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserMovieRepository extends JpaRepository<UserMovie, String> {
    Optional<UserMovie> findByUserAndMovie(User user, Movie movie);
}
