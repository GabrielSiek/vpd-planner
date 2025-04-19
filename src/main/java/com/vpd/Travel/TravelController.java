package com.vpd.Travel;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Collection.DTO.SearchCollectionDTO;
import com.vpd.Collection.DTO.SimpleCollectionDTO;
import com.vpd.Movie.DTO.MainPageMovieDTO;
import com.vpd.Movie.DTO.SearchMovieDTO;
import com.vpd.Travel.DTO.*;
import com.vpd.User.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "3 - Travel")
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
            @RequestBody RegisterTravelDTO travel) {

        ApiResponse<?> response = travelService.createTravel(travel);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{id}/rename")
    public ResponseEntity<ApiResponse<?>> renameTravel(
            @PathVariable String id,
            @RequestBody RenameTravelDTO newTravel,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.renameTravel(id, newTravel, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{id}/collections")
    public ResponseEntity<ApiResponse<List<SimpleCollectionDTO>>> getCollections(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<List<SimpleCollectionDTO>> response = travelService.getCollections(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/search-collections")
    public ResponseEntity<ApiResponse<List<SimpleCollectionDTO>>> searchCollections(
            @PathVariable String id,
            @RequestBody SearchCollectionDTO search,
            @AuthenticationPrincipal User user) {

        ApiResponse<List<SimpleCollectionDTO>> response = travelService.searchCollections(id, search, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @PutMapping("{id}/update-dates")
    public ResponseEntity<ApiResponse<?>> updateDates(
            @PathVariable String id,
            @RequestBody UpdateDatesDTO updateDatesDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.updateDates(id, updateDatesDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{id}/add-traveller")
    public ResponseEntity<ApiResponse<?>> addTraveller(
            @PathVariable String id,
            @RequestBody TravelInviteDTO travelInviteDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = travelService.addTraveller(id, travelInviteDTO, user);

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

    @PostMapping("{id}/search-movies")
    public ResponseEntity<ApiResponse<List<MainPageMovieDTO>>> searchMovies(
            @PathVariable String id,
            @RequestBody SearchMovieDTO searchMovieDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<List<MainPageMovieDTO>> response = travelService.searchMovie(id, searchMovieDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
