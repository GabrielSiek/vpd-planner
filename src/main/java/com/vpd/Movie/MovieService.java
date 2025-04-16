package com.vpd.Movie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Collection.Collection;
import com.vpd.Collection.CollectionRepository;
import com.vpd.Collection.DTO.SimpleCollectionDTO;
import com.vpd.Movie.DTO.MovieDTO;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Movie.DTO.RegisterMovieDTO;
import com.vpd.Travel.Travel;
import com.vpd.Travel.TravelRepository;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import com.vpd.UserMovie.UserMovie;
import com.vpd.UserMovie.UserMovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

//sem verificacao de usuario
@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private UserMovieRepository userMovieRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private TravelRepository travelRepository;

    public ApiResponse<?> addMovie(RegisterMovieDTO movieDTO, User user) {

        try {
            Optional<Travel> optionalTravel = user.getTravels().stream()
                    .filter(travel -> travel.getId().equals(movieDTO.travelId()))
                    .findFirst();

            if(optionalTravel.isEmpty())
                return ApiResponseHelper.notFound("Travel not found");

            Travel travel = optionalTravel.get();

            Movie movie = new Movie();
            movie.setTravel(travel);
            movie.setOriginalTitle(movieDTO.originalTitle());
            movie.setOriginalLanguage(movieDTO.originalLanguage());
            movie.setTitle(movieDTO.title());
            movie.setOverview(movieDTO.overview());
            movie.setGenres(getGenresFromIds(movieDTO.genres()));
            movie.setReleaseDate(movieDTO.releaseDate());
            movie.setVoteAverage(movieDTO.voteAverage());
            movie.setVoteCount(movieDTO.voteCount());
            movie.setPopularity(movieDTO.popularity());
            movie.setPosterPath(movieDTO.posterPath());
            movie.setBackgroundPath(movieDTO.backgroundPath());
            movie.setAdult(movieDTO.adult());

            List<UserMovie> userMovies = new ArrayList<>();

            for(User traveller : travel.getUsers()) {
                UserMovie userMovie = new UserMovie(traveller, movie);

                userMovies.add(userMovie);

                traveller.getUserMovies().add(userMovie);

                userMovieRepository.save(userMovie);
                userRepository.save(traveller);
            }

            movie.setUserMovies(userMovies);

            movieRepository.save(movie);

            return ApiResponseHelper.ok("Movie added successfully", movie.getTitle());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<MovieDTO> getMovie(String id, User user) {
        try {
            Optional<Movie> optionalMovie = movieRepository.findById(id);

            if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Movie movie = optionalMovie.get();
            Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

            if(optionalUserMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found on user collections");

            UserMovie userMovie = optionalUserMovie.get();

            List<SimpleCollectionDTO> collectionDTOList = movie.getCollections()
                    .stream().map(collection -> new SimpleCollectionDTO(
                            collection.getId(),
                            collection.getName()))
                    .toList();

            MovieDTO movieDTO = new MovieDTO(
                    movie.getTravel().getId(),
                    movie.getTravel().getName(),
                    collectionDTOList,
                    userMovie.getStars(),
                    userMovie.isFavorite(),
                    userMovie.isWatched(),
                    movie.getOriginalTitle(),
                    movie.getOriginalLanguage(),
                    movie.getTitle(),
                    movie.getOverview(),
                    movie.getGenres(),
                    movie.getReleaseDate(),
                    movie.getVoteAverage(),
                    movie.getVoteCount(),
                    movie.getPopularity(),
                    movie.getPosterPath(),
                    movie.getBackgroundPath(),
                    movie.isAdult()
            );

            return ApiResponseHelper.ok("Movie found successfully", movieDTO);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> deleteMovie(String id, User user) {

        try {

            Optional<Movie> optionalMovie = movieRepository.findById(id);

            if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Movie movie = optionalMovie.get();

            Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

            if(optionalUserMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            UserMovie userMovie = optionalUserMovie.get();

            Travel travel = userMovie.getMovie().getTravel();


            //remove relacao com os usuarios
            for(User traveller : travel.getUsers()) {
                userMovieRepository.deleteByUserAndMovie(traveller, movie);
                traveller.getUserMovies().remove(userMovie);

                userRepository.save(traveller);
            }

            //remove relacao com colecao
            for(Collection collection : movie.getCollections()) {
                collection.getMovies().remove(movie);

                collectionRepository.save(collection);
            }

            //remove relacao com viagem
            travel.getMovies().remove(movie);
            travelRepository.save(travel);

            return ApiResponseHelper.ok("Movie deleted successfully", movie.getTitle());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    private List<String> getGenresFromIds(List<String> ids) {
        return ids.stream()
                .map(id -> {
                    try {
                        return MovieGenre.getNameById(Integer.parseInt(id));
                    } catch (Exception exception) {
                        return null;

                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
