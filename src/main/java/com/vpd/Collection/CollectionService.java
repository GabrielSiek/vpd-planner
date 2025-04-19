package com.vpd.Collection;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Collection.DTO.*;
import com.vpd.Movie.DTO.MainPageMovieDTO;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Image.Image;
import com.vpd.Image.ImageService;
import com.vpd.Movie.DTO.SearchMovieDTO;
import com.vpd.Movie.Movie;
import com.vpd.Movie.MovieRepository;
import com.vpd.Travel.Travel;
import com.vpd.Travel.TravelRepository;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import com.vpd.UserMovie.UserMovie;
import com.vpd.UserMovie.UserMovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

//sem verificacao de usuario
@Service
public class CollectionService {

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserMovieRepository userMovieRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public ApiResponse<?> createCollection(RegisterCollectionDTO registerCollectionDTO) {

        try {
            Optional<Travel> optionalTravel = travelRepository.findById(registerCollectionDTO.travelId());

            if(optionalTravel.isEmpty())
                return ApiResponseHelper.notFound("Travel not found");

            Travel travel = optionalTravel.get();

            Set<Movie> movies = movieRepository.findByIdInAndTravel(registerCollectionDTO.moviesId(), travel);

        Collection collection = new Collection();

        collection.setName(registerCollectionDTO.name());
        collection.setTravel(travel);
        collection.setMovies(movies);

        Image poster = imageService.addImage(registerCollectionDTO.imageFile());

        if(poster != null)
            collection.setPoster(poster);

        collectionRepository.save(collection);

        return ApiResponseHelper.ok("Collection created succesfully", new SimpleCollectionDTO(collection.getId(), collection.getName()));

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public Image getPoster(String collectionId) {
        Optional<Collection> optionalCollection = collectionRepository.findById(collectionId);

        return optionalCollection.map(Collection::getPoster).orElse(null);
    }

    @Transactional
    public ApiResponse<?> updatePoster(String id, UpdatePosterCollectionDTO file) {
        try {

            Optional<Collection> optionalCollection = collectionRepository.findById(id);

            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            Collection collection = optionalCollection.get();

            MultipartFile poster = file.poster();

            Image newPoster = imageService.addImage(file.poster());

            if(newPoster == null)
                return ApiResponseHelper.badRequest("Poster without data");

            imageService.addImage(poster);

            collection.setPoster(newPoster);

            collectionRepository.save(collection);

            return ApiResponseHelper.ok("Poster updated successfully", newPoster.getImageName());

        }  catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> updateName(String id, UpdateNameCollectionDTO newName) {
        try {

            Optional<Collection> optionalCollection = collectionRepository.findById(id);

            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            Collection collection = optionalCollection.get();

            collection.setName(newName.name());

            collectionRepository.save(collection);

            return ApiResponseHelper.ok("Name updated successfully", newName.name());

        }  catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> addMovie(String id, MovieIdDTO movieIdDTO) {

        try {

            Optional<Collection> optionalCollection = collectionRepository.findById(id);
            Optional<Movie> optionalMovie = movieRepository.findById(movieIdDTO.movieId());

            if (optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            else if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Collection collection = optionalCollection.get();
            Movie movie = optionalMovie.get();

            collection.getMovies().add(optionalMovie.get());
            movie.getCollections().add(optionalCollection.get());

            collectionRepository.save(collection);
            movieRepository.save(movie);

            return ApiResponseHelper.ok("Movie added successfully", movie.getTitle());

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> removeMovie(String id, MovieIdDTO movieIdDTO) {

        try {

            Optional<Collection> optionalCollection = collectionRepository.findById(id);
            Optional<Movie> optionalMovie = movieRepository.findById(movieIdDTO.movieId());

            if (optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            else if(optionalMovie.isEmpty())
                return ApiResponseHelper.notFound("Movie not found");

            Collection collection = optionalCollection.get();
            Movie movie = optionalMovie.get();

            collection.getMovies().remove(movie);
            movie.getCollections().remove(collection);

            collectionRepository.save(collection);
            movieRepository.save(movie);

            return ApiResponseHelper.ok("Movie removed successfully", movie.getTitle());

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<List<MainPageMovieDTO>> getMovies(String id, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Optional<Collection> optionalCollection = collectionRepository.findById(id);

            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            List<MainPageMovieDTO> userMovieList = new ArrayList<>();

            Collection collection = optionalCollection.get();

            for (Movie movie : collection.getMovies()) {
                Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

                optionalUserMovie.ifPresent(userMovie -> userMovieList.add(new MainPageMovieDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getPosterPath(),
                        movie.getGenres(),
                        userMovie.getStars(),
                        userMovie.isFavorite(),
                        userMovie.isWatched()
                )));
            }

            return ApiResponseHelper.ok("Movies found successfully", userMovieList);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> deleteCollection(String id) {

        try {
            Optional<Collection> optionalCollection = collectionRepository.findById(id);

            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not Found");

            Collection collection = optionalCollection.get();

            Travel travel = collection.getTravel();

            travel.getCollections().remove(collection);

            collectionRepository.delete(collection);
            travelRepository.save(travel);

            imageService.deleteImage(collection.getPoster());

            return ApiResponseHelper.ok("Colletion deleted successfully", collection.getName());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<List<MainPageMovieDTO>> searchMovie(String id, SearchMovieDTO searchMovieDTO, User user) {

        try {
            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Optional<Collection> optionalCollection = collectionRepository.findById(id);

            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            Collection collection = optionalCollection.get();

            List<Movie> movies = collection.getMovies().stream()
                        .filter(movie -> {
                            boolean matchesTitle = true;
                            boolean matchesGenre = true;

                            if (searchMovieDTO.search() != null && !searchMovieDTO.search().isEmpty()) {
                                matchesTitle = movie.getTitle().toLowerCase().contains(searchMovieDTO.search().toLowerCase());
                            }

                            if (searchMovieDTO.genres() != null && !searchMovieDTO.genres().isEmpty()) {
                                matchesGenre = movie.getGenres().containsAll(searchMovieDTO.genres());
                            }

                            return matchesTitle && matchesGenre;
                        })
                        .toList();

            List<MainPageMovieDTO> mainPageMovieDTOS = new ArrayList<>();

            for(Movie movie : movies) {
                Optional<UserMovie> optionalUserMovie = userMovieRepository.findByUserAndMovie(user, movie);

                UserMovie userMovie;

                if(optionalUserMovie.isEmpty()) {
                    userMovie = new UserMovie(user, movie);
                    userMovieRepository.save(userMovie);
                    System.out.println("User Movie relation created to: " + movie.getTitle() + "and " + user.getUsername());
                } else {
                    userMovie = optionalUserMovie.get();
                }

                MainPageMovieDTO mainPageMovieDTO = new MainPageMovieDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getPosterPath(),
                        movie.getGenres(),
                        userMovie.getStars(),
                        userMovie.isFavorite(),
                        userMovie.isWatched());

                mainPageMovieDTOS.add(mainPageMovieDTO);
            }

            return ApiResponseHelper.ok("Movies found: " + mainPageMovieDTOS.size(), mainPageMovieDTOS);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

}