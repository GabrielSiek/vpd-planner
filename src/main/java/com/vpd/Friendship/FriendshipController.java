package com.vpd.Friendship;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("friendship")
public class FriendshipController {

    @Autowired
    private FriendShipService friendShipService;

    @PostMapping("invite")
    public ResponseEntity<ApiResponse<?>> sendInvite(
            @RequestBody FriendshipInviteDTO friendshipInviteDTO,
            @AuthenticationPrincipal User requester
            ) {

        ApiResponse<?> response = friendShipService.sendInvite(friendshipInviteDTO, requester);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
