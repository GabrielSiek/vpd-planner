package com.vpd.User;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Friendship.Friendship;
import com.vpd.Travel.DTO.TravelBasicDTO;
import com.vpd.Travel.DTO.TravellerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public ApiResponse<List<TravelBasicDTO>> getTravels(User user) {

        try {

            Optional<User> optionalUser = userRepository.findById(user.getId());

            if(optionalUser.isEmpty())
                return ApiResponseHelper.notFound("User not found");

            user = optionalUser.get();

            List<TravelBasicDTO> travels = user.getTravels().stream()
                    .map(travel -> new TravelBasicDTO(
                            travel.getId(),
                            travel.getName(),
                            travel.getCollections().size(),
                            travel.getMovies().size(),
                            travel.getUsers().stream()
                                    .map(traveller -> new TravellerDTO(
                                            traveller.getEmail(),
                                            traveller.getUsername()
                                    )).toList(),
                            travel.getStartDate(),
                            travel.getEndDate()
                    )).toList();

            return ApiResponseHelper.ok("Total travels: " + travels.size(), travels);
        } catch (Exception exception) {
            return  ApiResponseHelper.internalError(exception);
        }
    }
}
