package com.vpd.UserMovie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Movie.Movie;
import com.vpd.Movie.MovieRepository;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserMovieService {

    @Autowired
    private UserMovieRepository userMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

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

    public ApiResponse<?> changeFavoriteStatus(String id, User user, boolean status) {

        try {
            boolean NewStatus = !status;

            Optional<Movie> optionalMovie = movieRepository.findById(id);

            if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Movie movie = optionalMovie.get();

            Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

            if(optionalUserMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            UserMovie userMovie = optionalUserMovie.get();

            userMovie.setFavorite(NewStatus);

            userMovieRepository.save(userMovie);

            return ApiResponseHelper.ok("favorite set to: " + NewStatus, userMovie.getMovie().getTitle());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> changeViewStatus(String id, User user, boolean status) {

        try {
            boolean NewStatus = !status;

            Optional<Movie> optionalMovie = movieRepository.findById(id);

            if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Movie movie = optionalMovie.get();

            Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

            if(optionalUserMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            UserMovie userMovie = optionalUserMovie.get();

            userMovie.setWatched(NewStatus);

            userMovieRepository.save(userMovie);

            return ApiResponseHelper.ok("Watched set to: " + NewStatus, userMovie.getMovie().getTitle());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> updateStars(String id, User user, double review) {
        try {

            Optional<Movie> optionalMovie = movieRepository.findById(id);

            if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Movie movie = optionalMovie.get();

            Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

            if(optionalUserMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            UserMovie userMovie = optionalUserMovie.get();

            userMovie.setStars(review);

            userMovieRepository.save(userMovie);

            return ApiResponseHelper.ok("Stars set to: " + review, userMovie.getMovie().getTitle());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }
}
