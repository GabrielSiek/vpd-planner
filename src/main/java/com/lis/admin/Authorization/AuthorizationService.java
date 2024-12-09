package com.lis.admin.Authorization;

import com.lis.admin.User.User;
import com.lis.admin.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Verifica se o usuário existe no banco de dados
        User user = userRepository.findByEmail(username);

        if (user == null) {
            // Caso o usuário não seja encontrado, lança uma exceção
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        // Retorna o usuário caso encontrado
        return user;
    }
}
