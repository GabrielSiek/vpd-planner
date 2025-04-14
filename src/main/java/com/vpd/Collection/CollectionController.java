package com.vpd.Collection;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Collection.DTO.PosterCollectionDTO;
import com.vpd.Collection.DTO.RegisterCollectionDTO;
import com.vpd.Image.Image;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("collection")
public class CollectionController {

    @Autowired
    CollectionService collectionService;

    @PostMapping("create")
    public ResponseEntity<ApiResponse<?>> createCollection(
            @RequestBody RegisterCollectionDTO registerCollectionDTO,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = collectionService.createCollection(registerCollectionDTO, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/poster")
    public ResponseEntity<byte[]> getCollectionPoster(
            @RequestBody PosterCollectionDTO posterCollectionDTO,
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        Image poster = collectionService.getPoster(id, posterCollectionDTO, user);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(poster.getImageType()))
                .body(poster.getImageData());
    }
}
