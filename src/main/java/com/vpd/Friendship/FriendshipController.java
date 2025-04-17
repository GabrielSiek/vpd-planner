package com.vpd.Friendship;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Friendship.DTO.FriendDTO;
import com.vpd.Friendship.DTO.FriendshipDTO;
import com.vpd.Friendship.DTO.FriendshipInviteDTO;
import com.vpd.Friendship.DTO.FriendshipResponseDTO;
import com.vpd.User.User;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "2 - Friendship")
@RestController
@RequestMapping("friendship")
public class FriendshipController {

    @Autowired
    private FriendShipService friendShipService;

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<FriendshipDTO>> getFriendship(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<FriendshipDTO> response = friendShipService.getFriendship(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("all")
    public ResponseEntity<ApiResponse<List<FriendDTO>>> getAllFriendships(@AuthenticationPrincipal User user) {

        ApiResponse<List<FriendDTO>> response = friendShipService.getAllFriendships(user);

        return  ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("invites")
    public ResponseEntity<ApiResponse<List<FriendDTO>>> getAllInvites(@AuthenticationPrincipal User user) {

        ApiResponse<List<FriendDTO>> response = friendShipService.getAllInvites(user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("invite")
    public ResponseEntity<ApiResponse<?>> sendInvite(
            @RequestBody FriendshipInviteDTO friendshipInviteDTO,
            @AuthenticationPrincipal User requester
            ) {

        ApiResponse<?> response = friendShipService.sendInvite(friendshipInviteDTO, requester);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("{id}/response")
    public ResponseEntity<ApiResponse<?>> responseInvite(
            @PathVariable String id,
            @RequestBody FriendshipResponseDTO friendshipResponse,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = friendShipService.responseInvite(id, friendshipResponse, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("{id}/delete")
    public ResponseEntity<ApiResponse<?>> deleteFriendship(
            @PathVariable String id,
            @AuthenticationPrincipal User user) {

        ApiResponse<?> response = friendShipService.deleteFriendship(id, user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
