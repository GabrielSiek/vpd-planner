package com.vpd.Travel;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.ApiResponse.ApiResponseHelper;
import com.vpd.Travel.DTO.*;
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

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<TravelBasicDTO>> getBasicTravelInformation (
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<TravelBasicDTO> response = travelService.getBasicTravelInformation(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("create")
    public ResponseEntity<ApiResponse<?>> createTravel(
            @RequestBody RegisterTravelDTO travel,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.createTravel(travel, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("rename")
    public ResponseEntity<ApiResponse<?>> renameTravel(
            @RequestBody RenameTravelDTO newTravel,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.renameTravel(newTravel, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("update-dates")
    public ResponseEntity<ApiResponse<?>> updateDates(
            @RequestBody UpdateDatesDTO updateDatesDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.updateDates(updateDatesDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("add-traveller")
    public ResponseEntity<ApiResponse<?>> addTraveller(
            @RequestBody TravelInviteDTO travelInviteDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.addTraveller(travelInviteDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{id}/leave")
    public ResponseEntity<ApiResponse<?>> leaveTravel(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.leaveTravel(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<ApiResponse<?>> deleteTravel(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.deleteTravel(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
