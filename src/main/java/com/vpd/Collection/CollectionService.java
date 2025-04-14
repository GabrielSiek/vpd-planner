package com.vpd.Collection;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Collection.DTO.PosterCollectionDTO;
import com.vpd.Collection.DTO.RegisterCollectionDTO;
import com.vpd.Image.Image;
import com.vpd.Image.ImageService;
import com.vpd.Movie.Movie;
import com.vpd.Movie.MovieRepository;
import com.vpd.Travel.Travel;
import com.vpd.Travel.TravelRepository;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

    public Image getPoster(String id, PosterCollectionDTO posterCollectionDTO, User user) {
        return collectionRepository.findAuthorizedCollection(id, posterCollectionDTO.travelId(), user)
                .map(Collection::getPoster)
                .orElse(null);
    }
}