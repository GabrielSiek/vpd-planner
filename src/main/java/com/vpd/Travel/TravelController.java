package com.vpd.Travel;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Travel.DTO.RegisterTravelDTO;
import com.vpd.Travel.DTO.TravelBasicDTO;
import com.vpd.Travel.DTO.TravelInviteDTO;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("travel")
public class TravelController {

    @Autowired
    private TravelService travelService;

    @GetMapping("{id}/basic")
    public ResponseEntity<ApiResponse<TravelBasicDTO>> getBasicTravelInformation (
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<TravelBasicDTO> response = travelService.getBasicTravelInformation(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    //ver as coisas da viagem

    @PostMapping("create")
    public ResponseEntity<ApiResponse<?>> createTravel(
            @RequestBody RegisterTravelDTO travel,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.createTravel(travel, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("invite")
    public ResponseEntity<ApiResponse<?>> addTraveller(
            @RequestBody TravelInviteDTO travelInviteDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.addTraveller(travelInviteDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    //deletar
}
