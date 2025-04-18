package com.vpd.UserMovie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.User.User;
import com.vpd.UserMovie.DTO.UpdateFavoriteStatusDTO;
import com.vpd.UserMovie.DTO.UpdateStarsDTO;
import com.vpd.UserMovie.DTO.UpdateViewStatusDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "6 - Movie variables")
@RestController
@RequestMapping("movie")
public class UserMovieController {

    @Autowired
    UserMovieService userMovieService;

    @PutMapping("{id}/update-favorite-status")
    public ResponseEntity<ApiResponse<?>> changeFavoriteStatus(
            @RequestBody UpdateFavoriteStatusDTO status,
            @PathVariable String id,
            @AuthenticationPrincipal User user
            ) {

        ApiResponse<?> response = userMovieService.changeFavoriteStatus(id, user, status.favoriteStatus());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{id}/update-view-status")
    public ResponseEntity<ApiResponse<?>> changeViewStatus(
            @RequestBody UpdateViewStatusDTO status,
            @PathVariable String id,
            @AuthenticationPrincipal User user
    ) {

        ApiResponse<?> response = userMovieService.changeViewStatus(id, user, status.viewStatus());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{id}/update-review-status")
    public ResponseEntity<ApiResponse<?>> changeReviewStatus(
            @RequestBody UpdateStarsDTO review,
            @PathVariable String id,
            @AuthenticationPrincipal User user
    ) {

        ApiResponse<?> response = userMovieService.updateStars(id, user, review.stars());

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
