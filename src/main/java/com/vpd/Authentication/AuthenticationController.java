package com.vpd.Authentication;

import com.vpd.Authentication.DTO.AuthenticationDTO;
import com.vpd.Authentication.DTO.LoginResponseDTO;
import com.vpd.Infra.Security.TokenService;
import com.vpd.User.User;
import com.vpd.User.DTO.UserDTO;
import com.vpd.User.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "1 - Authentication")
@RestController
@RequestMapping("auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO authenticationDTO) {

        var usernamePassword = new UsernamePasswordAuthenticationToken(authenticationDTO.email(), authenticationDTO.password());
        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid UserDTO userDTO) {
        if(this.userRepository.findByEmail(userDTO.email()) != null)
            return ResponseEntity.badRequest().body("Email already registered on system");

        if(this.userRepository.findByUsername(userDTO.username()) != null)
            return ResponseEntity.badRequest().body("Username already registered on system");

        String encryptedPassword = new BCryptPasswordEncoder().encode(userDTO.password());

        User newUser = new User(userDTO.email(), userDTO.username(), encryptedPassword);

        userRepository.save(newUser);

        return ResponseEntity.ok().body("User registered successfully");
    }
}
