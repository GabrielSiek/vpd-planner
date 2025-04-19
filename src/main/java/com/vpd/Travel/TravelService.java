package com.vpd.Travel;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Collection.DTO.SearchCollectionDTO;
import com.vpd.Collection.DTO.SimpleCollectionDTO;
import com.vpd.Movie.DTO.MainPageMovieDTO;
import com.vpd.Movie.DTO.SearchMovieDTO;
import com.vpd.Movie.Movie;
import com.vpd.Movie.MovieRepository;
import com.vpd.Travel.DTO.*;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import com.vpd.UserMovie.UserMovie;
import com.vpd.UserMovie.UserMovieRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMovieRepository userMovieRepository;

    @Autowired
    private MovieRepository movieRepository;

    public ApiResponse<TravelBasicDTO> getBasicTravelInformation(String id, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Optional<Travel> optionalTravel = travelRepository.findByIdAndUser(id, user);

            if (optionalTravel.isEmpty())
                return ApiResponseHelper.badRequest("Travel not found");

            Travel travel = optionalTravel.get();

            if (!travel.getUsers().contains(user))
                return ApiResponseHelper.unauthorized();

            List<TravellerDTO> travellers = travel.getUsers().stream()
                    .map(traveller -> new TravellerDTO(traveller.getEmail(), traveller.getUsername()))
                    .toList();

            TravelBasicDTO travelBasicDTO = new TravelBasicDTO(
                    travel.getId(),
                    travel.getName(),
                    travel.getCollections().size(),
                    travel.getMovies().size(),
                    travellers,
                    travel.getStartDate(),
                    travel.getEndDate());

            return ApiResponseHelper.ok("Travel found successfully", travelBasicDTO);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> createTravel(RegisterTravelDTO travelDTO) {
        try {
            Travel travel = new Travel();
            travel.setName(travelDTO.name());
            travel.setStartDate(travelDTO.startDate());
            travel.setEndDate(travelDTO.endDate());

            if (!travelDTO.emails().isEmpty()) {
                Set<User> users = travelDTO.emails().stream()
                        .map(email -> userRepository.findByEmail(email))
                        .collect(Collectors.toSet());

                if (users.contains(null)) {
                    return ApiResponseHelper.badRequest("Travellers list incorrect");
                }

                travel.setUsers(users);

                for (User user : users) {
                    user.getTravels().add(travel);
                }
            }

            travelRepository.save(travel);

            return ApiResponseHelper.ok("Travel created successfully", travel);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> renameTravel(String id, RenameTravelDTO newTravel, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Travel travel = travelRepository.findByIdAndUser(id, user).orElse(null);

            if(travel == null)
                return  ApiResponseHelper.notFound("Travel not found");

            travel.setName(newTravel.newName());
            travelRepository.save(travel);

            return ApiResponseHelper.ok("Travel renamed successfully", travel.getName());

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> updateDates(String id, UpdateDatesDTO updateDatesDTO, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Travel travel = travelRepository.findByIdAndUser(id, user).orElse(null);

            if (travel == null)
                return ApiResponseHelper.notFound("Travel not found");

            travel.setStartDate(updateDatesDTO.startDate());
            travel.setEndDate(updateDatesDTO.endDate());


            travelRepository.save(travel);


            return ApiResponseHelper.ok("Dates updated successfully", updateDatesDTO);
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> addTraveller(String id, TravelInviteDTO travelInviteDTO, User user) {
        try {
            user = userRepository.findById(user.getId())
                    .orElse(null);
            if (user == null)
                return ApiResponseHelper.notFound("User not found");

            User receiver = userRepository.findByEmail(travelInviteDTO.email());
            if (receiver == null)
                return ApiResponseHelper.notFound("Invited user not found");

            Travel travel = travelRepository.findByIdAndUser(id, user).orElse(null);
            if (travel == null)
                return ApiResponseHelper.notFound("Travel not found");

            if (travel.getUsers().stream().anyMatch(u -> u.getId().equals(receiver.getId()))) {
                return ApiResponseHelper.badRequest("User already added to the travel");
            }

            travel.getUsers().add(receiver);
            receiver.getTravels().add(travel);

            userRepository.save(receiver);

            for(Movie movie : travel.getMovies()) {
                UserMovie userMovie = new UserMovie(receiver, movie);

                userMovieRepository.save(userMovie);
            }

            return ApiResponseHelper.ok("Traveller added successfully", travel);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> leaveTravel(String id, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Travel travel = travelRepository.findByIdAndUser(id, user).orElse(null);

            if (travel == null)
                return ApiResponseHelper.notFound("Travel not Found");

            travel.getUsers().remove(user);
            user.getTravels().remove(travel);

            travelRepository.save(travel);

            return ApiResponseHelper.ok("Leaved travel successfully", travel.getName());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> deleteTravel(String id, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Travel travel = travelRepository.findByIdAndUser(id, user).orElse(null);

            if (travel == null)
                return ApiResponseHelper.notFound("Travel not found");

            Set<User> users = travel.getUsers();
            for (User u : users) {
                u.getTravels().remove(travel);
                userRepository.save(u);
            }

            travelRepository.delete(travel);

            return ApiResponseHelper.ok("Travel deleted", travel.getName());
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

            Optional<Travel> optionalTravel = travelRepository.findByIdAndUser(id, user);

            if(optionalTravel.isEmpty())
                return ApiResponseHelper.notFound("Travel not found");
            
            Travel travel = optionalTravel.get();

            List<Movie> movies = travel.getMovies().stream()
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

                if(optionalUserMovie.isEmpty())
                    return ApiResponseHelper.badRequest("Error finding movies for this user");

                MainPageMovieDTO mainPageMovieDTO = new MainPageMovieDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getPosterPath(),
                        movie.getGenres(),
                        optionalUserMovie.get().getStars(),
                        optionalUserMovie.get().isFavorite(),
                        optionalUserMovie.get().isWatched());

                mainPageMovieDTOS.add(mainPageMovieDTO);
            }


            return ApiResponseHelper.ok("Movies found: " + mainPageMovieDTOS.size(), mainPageMovieDTOS);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<List<SimpleCollectionDTO>> getCollections(String id, User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if (optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Optional<Travel> optionalTravel = travelRepository.findByIdAndUser(id, user);

            if (optionalTravel.isEmpty())
                return ApiResponseHelper.notFound("Travel not found");

            Travel travel = optionalTravel.get();

            List<SimpleCollectionDTO> collections = travel.getCollections().stream()
                    .map(collection -> new SimpleCollectionDTO(
                            collection.getId(),
                            collection.getName()))
                    .toList();

            return ApiResponseHelper.ok("Total collections: " + collections.size(), collections);
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<List<SimpleCollectionDTO>> searchCollections(String id, SearchCollectionDTO search, User user) {

        try {
            Optional<User> optionalUser = userRepository.findById(user.getId());

            if (optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            Optional<Travel> optionalTravel = travelRepository.findByIdAndUser(id, user);

            if (optionalTravel.isEmpty())
                return ApiResponseHelper.notFound("Travel not found");

            Travel travel = optionalTravel.get();

            List<SimpleCollectionDTO> collections = travel.getCollections().stream()
                    .filter(collection -> collection.getName().toLowerCase().contains(search.collectionName().toLowerCase()))
                    .map(collection -> new SimpleCollectionDTO(
                            collection.getId(),
                            collection.getName()))
                    .toList();

            return ApiResponseHelper.ok("Total collections: " + collections.size(), collections);
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

}
