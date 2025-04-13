package com.vpd.Travel;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Travel.DTO.RegisterTravelDTO;
import com.vpd.Travel.DTO.TravelBasicDTO;
import com.vpd.Travel.DTO.TravelInviteDTO;
import com.vpd.Travel.DTO.TravellerDTO;
import com.vpd.User.User;
import com.vpd.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TravelService {
    
    @Autowired
    private TravelRepository travelRepository;

    @Autowired
    private UserRepository userRepository;
    
    public ApiResponse<TravelBasicDTO> getBasicTravelInformation(String id, User user) {
        
        try {
            Optional<Travel> optionalTravel = travelRepository.findById(id);
            
            if(optionalTravel.isEmpty())
                return ApiResponseHelper.badRequest("Unable to find travel with ID provided");
            
            Travel travel = optionalTravel.get();
            
            if(!travel.getUsers().contains(user))
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

    public ApiResponse<?> createTravel(RegisterTravelDTO travelDTO, User user) {

        try {
            Travel travel = new Travel();

            travel.setName(travel.getName());
            travel.setStartDate(travelDTO.startDate());
            travel.setEndDate(travelDTO.endDate());


        } catch (Exception exception) {
            return ApiResponseHelper.internalError(exception);
        }
    }

    public ApiResponse<?> addTraveller(TravelInviteDTO travelInviteDTO, User user) {

        try {

            Optional<User> receiver = Optional.ofNullable(userRepository.findByEmail(travelInviteDTO.email()));

            if(receiver.isEmpty())
                return ApiResponseHelper.notFound("User not found");


            List<String> travelsId = user.getTravels().stream()
                    .map(Travel::getId)
                    .toList();

            Optional<Travel> optionalTravel = travelRepository.findById(travelInviteDTO.id());

            if(!travelsId.contains(travelInviteDTO.id()) || optionalTravel.isEmpty())
                return ApiResponseHelper.notFound("Travel not Found");

            Travel travel = optionalTravel.get();

            Set<User> users = travel.getUsers();

            for(User u : users) {
                if(travelInviteDTO.id().equals(u.getId()))
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
}
