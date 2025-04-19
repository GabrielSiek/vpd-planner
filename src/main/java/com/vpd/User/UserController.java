package com.vpd.User;

import com.vpd.ApiResponse.ApiResponse;
import com.vpd.Travel.DTO.TravelBasicDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/travels")
    public ResponseEntity<ApiResponse<List<TravelBasicDTO>>> getTravels(@AuthenticationPrincipal User user) {

        ApiResponse<List<TravelBasicDTO>> response = userService.getTravels(user);

        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
