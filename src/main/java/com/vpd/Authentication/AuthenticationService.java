package com.vpd.Authentication;

import com.vpd.Infra.Security.TokenService;
import com.vpd.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private TokenService tokenService;   // Agora TokenService é usado aqui
    @Autowired
    private UserRepository userRepository;  // Usado para encontrar o usuário

    public String validateTokenAndGetUsername(String token) {
        return tokenService.validateToken(token);  // Valida o token e retorna o username
    }

    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username);  // Carrega o usuário pelo email
    }
}
