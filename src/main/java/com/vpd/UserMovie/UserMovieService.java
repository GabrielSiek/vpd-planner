package com.vpd.UserMovie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Movie.Movie;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;

public class UserMovieService {

    @Autowired
    private UserMovieRepository userMovieRepository;

    public UserMovie createUserMovie(Movie movie, User user) {

        try {
            UserMovie userMovie = new UserMovie();

            userMovie.setUser(user);
            userMovie.setMovie(movie);

            userMovieRepository.save(userMovie);

            return userMovie;
        } catch (Exception exception) {
            return null;
        }
    }

    //atualizar valores

    //deletar usermovie(?)
}
