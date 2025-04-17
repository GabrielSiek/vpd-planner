package com.vpd.Collection;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Collection.DTO.RegisterCollectionDTO;
import com.vpd.Collection.DTO.UpdateNameCollectionDTO;
import com.vpd.Movie.DTO.MainPageMovieDTO;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Collection.DTO.UpdatePosterCollectionDTO;
import com.vpd.Image.Image;
import com.vpd.User.User;
import com.vpd.UserMovie.DTO.UserMovieDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "4 - Collections")
@RestController
@RequestMapping("collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @PostMapping("create")
    public ResponseEntity<ApiResponse<?>> createCollection(
            @RequestBody RegisterCollectionDTO registerCollectionDTO) {

        ApiResponse<?> response = collectionService.createCollection(registerCollectionDTO);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/update-collection")
    public ResponseEntity<ApiResponse<?>> updatePoster(
            @PathVariable String id,
            @RequestBody UpdatePosterCollectionDTO newPoster) {

        ApiResponse<?> response = collectionService.updatePoster(id ,newPoster);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/update-name")
    public ResponseEntity<ApiResponse<?>> updateName(
            @PathVariable String id,
            @RequestBody UpdateNameCollectionDTO newName) {

        ApiResponse<?> response = collectionService.updateName(id ,newName);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{id}/poster")
    public ResponseEntity<byte[]> getCollectionPoster(@PathVariable String id) {

        Image poster = collectionService.getPoster(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(poster.getImageType()))
                .body(poster.getImageData());
    }

    @PostMapping("{id}/add-movie")
    public ResponseEntity<ApiResponse<?>> addMovie(
            @PathVariable String id,
            @RequestBody MovieIdDTO movieIdDTO) {

        ApiResponse<?> response = collectionService.addMovie(id, movieIdDTO);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{id}/remove-movie")
    public ResponseEntity<ApiResponse<?>> removeMovie(
            @PathVariable String id,
            @RequestBody MovieIdDTO movieIdDTO) {

        ApiResponse<?> response = collectionService.removeMovie(id, movieIdDTO);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("{id}/movies")
    public ResponseEntity<ApiResponse<List<MainPageMovieDTO>>> getMovies(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<List<MainPageMovieDTO>> response = collectionService.getMovies(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<ApiResponse<?>> deleteCollection(@PathVariable String id) {

        ApiResponse<?> response = collectionService.deleteCollection(id);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
