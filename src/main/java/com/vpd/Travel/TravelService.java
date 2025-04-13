package com.vpd.Travel;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Travel.DTO.RegisterTravelDTO;
import com.vpd.Travel.DTO.TravelBasicDTO;
import com.vpd.Travel.DTO.TravelInviteDTO;
import com.vpd.Travel.DTO.TravellerDTO;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TravelService {

    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<TravelBasicDTO> getBasicTravelInformation(String id, User user) {

        try {
            Optional<Travel> optionalTravel = travelRepository.findById(id);

            if (optionalTravel.isEmpty())
                return ApiResponseHelper.badRequest("Unable to find travel with ID provided");

            Travel travel = optionalTravel.get();

            if (!travel.getUsers().contains(user))
                return ApiResponseHelper.unauthorized();

            List<TravellerDTO> travellers = travel.getUsers().stream()
                    .map(traveller -> new TravellerDTO(traveller.getEmail(), traveller.getUsername()))
                    .toList();

            TravelBasicDTO travelBasicDTO = new TravelBasicDTO(
                    travel.getId(),
                    travel.getCollections().size(),
                    travel.getMovies().size(),
                    travellers,
                    travel.getStartDate(),
                    travel.getEndDate());

            return ApiResponseHelper.ok("Showing " + travel.getName() + "info", travelBasicDTO);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    @Transactional
    public ApiResponse<?> createTravel(RegisterTravelDTO travelDTO, User user) {

        try {
            Travel travel = new Travel();

            travel.setName(travel.getName());
            travel.setStartDate(travelDTO.startDate());
            travel.setEndDate(travelDTO.endDate());

            if(!travelDTO.emails().isEmpty()) {
                Set<User> users = travelDTO.emails().stream()
                        .map(email -> userRepository.findByEmail(email))
                        .collect(Collectors.toSet());

                if (users.contains(null))
                    return ApiResponseHelper.badRequest("Travellers list incorrect");

                travel.setUsers(users);

                for(User u : users){
                    u.getTravels().add(travel);
                    userRepository.save(u);
                }
            }

            travelRepository.save(travel);

            return ApiResponseHelper.ok("Travel created succesfully", travel.getName());

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> addTraveller(TravelInviteDTO travelInviteDTO, User user) {

        try {

            Optional<User> receiver = Optional.ofNullable(userRepository.findByEmail(travelInviteDTO.email()));

            if (receiver.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            Travel travel = getTravelFromUser(travelInviteDTO.id(), user);

            if (travel == null)
                return ApiResponseHelper.notFound("Travel not Found");

            Set<User> users = travel.getUsers();

            for (User u : users) {
                if (travelInviteDTO.id().equals(u.getId()))
                    return ApiResponseHelper.badRequest("User already added to the travel");
            }

            travel.getUsers().add(user);

            user.getTravels().add(travel);

            travelRepository.save(travel);

            userRepository.save(user);

            return ApiResponseHelper.ok("Traveller added successfully", travel);

        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> leaveTravel(String id, User user) {

        try {
            Travel travel = getTravelFromUser(id, user);

            if (travel == null)
                return ApiResponseHelper.notFound("Travel not Found");

            return ApiResponseHelper.ok("Leaved travel successfully", travel.getName());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> deleteTravel(String id, User user) {

        try {

            Travel travel = getTravelFromUser(id, user);

            if (travel == null)
                return ApiResponseHelper.notFound("Travel not found");

            travelRepository.delete(travel);

            return ApiResponseHelper.ok("Travel deleted", travel.getName());
        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    private Travel getTravelFromUser(String id, User user) {

        try {
            Set<Travel> travels = user.getTravels();

            Travel travel = null;

            for (Travel t : travels) {
                if (t.getId().equals(id))
                    travel = t;
            }

            return travel;
        } catch (Exception exception) {
            return null;
        }
    }
}
