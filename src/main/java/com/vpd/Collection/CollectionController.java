package com.vpd.Collection;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Collection.DTO.RegisterCollectionDTO;
import com.vpd.Collection.DTO.UpdateNameCollectionDTO;
import com.vpd.Movie.DTO.MainPageMovieDTO;
import com.vpd.Movie.DTO.MovieIdDTO;
import com.vpd.Collection.DTO.UpdatePosterCollectionDTO;
import com.vpd.Image.Image;
import com.vpd.Movie.DTO.SearchMovieDTO;
import com.vpd.User.User;
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

    @PostMapping(value = "create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createCollection(
            @ModelAttribute RegisterCollectionDTO registerCollectionDTO) {

        ApiResponse<?> response = collectionService.createCollection(registerCollectionDTO);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{id}/update-poster")
    public ResponseEntity<ApiResponse<?>> updatePoster(
            @PathVariable String id,
            @ModelAttribute UpdatePosterCollectionDTO newPoster) {

        ApiResponse<?> response = collectionService.updatePoster(id ,newPoster);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("{id}/update-name")
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

    @PutMapping("{id}/add-movie")
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

    @PostMapping("{id}/search-movies")
    public ResponseEntity<ApiResponse<List<MainPageMovieDTO>>> searchMovies(
            @PathVariable String id,
            @RequestBody SearchMovieDTO searchMovieDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<List<MainPageMovieDTO>> response = collectionService.searchMovie(id, searchMovieDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
