package com.vpd.UserMovie;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("movie")
public class UserMovieController {

    @Autowired
    UserMovieService userMovieService;

    @PostMapping("{id}/change-favorite-status")
    public ResponseEntity<ApiResponse<?>> changeFavoriteStatus(
            @RequestBody boolean status,
            @PathVariable String id,
            @AuthenticationPrincipal User user
            ) {

        ApiResponse<?> response = userMovieService.changeFavoriteStatus(id, user, status);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/change-view-status")
    public ResponseEntity<ApiResponse<?>> changeViewStatus(
            @RequestBody boolean status,
            @PathVariable String id,
            @AuthenticationPrincipal User user
    ) {

        ApiResponse<?> response = userMovieService.changeViewStatus(id, user, status);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/change-view-status")
    public ResponseEntity<ApiResponse<?>> changeReviewStatus(
            @RequestBody double review,
            @PathVariable String id,
            @AuthenticationPrincipal User user
    ) {

        ApiResponse<?> response = userMovieService.updateStars(id, user, review);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
