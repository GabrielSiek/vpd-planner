package com.vpd.Collection;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Collection.DTO.RegisterCollectionDTO;
import com.vpd.Collection.DTO.UpdateNameCollectionDTO;
import com.vpd.Movie.DTO.MainPageMovieDTO;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Collection.DTO.UpdatePosterCollectionDTO;
import com.vpd.Image.Image;
import com.vpd.Image.ImageService;
import com.vpd.Movie.Movie;
import com.vpd.Movie.MovieRepository;
import com.vpd.Travel.Travel;
import com.vpd.Travel.TravelRepository;
import com.vpd.User.User;
import com.vpd.UserMovie.DTO.UserMovieDTO;
import com.vpd.UserMovie.UserMovie;
import com.vpd.UserMovie.UserMovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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


    @Transactional
    public ApiResponse<?> createCollection(RegisterCollectionDTO registerCollectionDTO, User user) {

        try {
            Optional<Travel> optionalTravel = travelRepository.findByIdAndUser(registerCollectionDTO.travelId(), user);

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

        return ApiResponseHelper.ok("Collection created succesfully", collection.getName());

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

            MultipartFile poster = file.poster();

            Image newPoster = imageService.addImage(file.poster());

            if(newPoster == null)
                return ApiResponseHelper.badRequest("Poster without data");

            imageService.addImage(poster);

            optionalCollection.get().setPoster(newPoster);

            collectionRepository.save(optionalCollection.get());

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

            optionalCollection.get().setName(newName.name());

            collectionRepository.save(optionalCollection.get());

            return ApiResponseHelper.ok("Poster updated successfully", newName.name());

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

            optionalCollection.get().getMovies().add(optionalMovie.get());
            optionalMovie.get().getCollections().add(optionalCollection.get());

            collectionRepository.save(optionalCollection.get());
            movieRepository.save(optionalMovie.get());

            return ApiResponseHelper.ok("Movie added successfully", optionalMovie.get().getTitle());

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

            optionalCollection.get().getMovies().remove(optionalMovie.get());
            optionalMovie.get().getCollections().remove(optionalCollection.get());

            collectionRepository.save(optionalCollection.get());
            movieRepository.save(optionalMovie.get());

            return ApiResponseHelper.ok("Movie removed successfully", optionalMovie.get().getTitle());

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<List<MainPageMovieDTO>> getMovies(String id, User user) {

        try {
            Optional<Collection> optionalCollection = collectionRepository.findById(id);


            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not found");

            List<MainPageMovieDTO> userMovieList = new ArrayList<>();

            for (Movie movie : optionalCollection.get().getMovies()) {
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
    public ApiResponse<?> deleteCollection(String id, User user) {

        try {
            Optional<Collection> optionalCollection = collectionRepository.findById(id);

            if(optionalCollection.isEmpty())
                return ApiResponseHelper.notFound("Collection not Found");

            if(!optionalCollection.get().getTravel().getUsers().contains(user))
                return ApiResponseHelper.unauthorized();

            Travel travel = optionalCollection.get().getTravel();

            travel.getCollections().remove(optionalCollection.get());

            collectionRepository.delete(optionalCollection.get());
            travelRepository.save(travel);

            imageService.deleteImage(optionalCollection.get().getPoster());

            return ApiResponseHelper.ok("Colletion deleted successfully", optionalCollection.get().getName());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }
}