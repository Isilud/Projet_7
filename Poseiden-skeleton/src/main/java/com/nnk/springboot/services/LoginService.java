package com.nnk.springboot.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.model.User;
import com.nnk.springboot.repositories.UserRepository;

/**
 * Service de gestion des utilisateurs pour l'authentification.
 * 
 * Cette classe implémente l'interface {@link UserDetailsService} nécessaire
 * à la sécurité de Spring.
 */
@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructeur du service d'authentification.
     * 
     * @param userRepository Le repository contenant les utilisateurs.
     */
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son nom d'utilisateur.
     * 
     * @param username Le nom d'utilisateur de l'utilisateur à rechercher.
     * @return Un objet {@link UserDetails} contenant les informations de
     *         l'utilisateur.
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> foundUser = userRepository.findByUsername(username);
        if (foundUser.isPresent()) {
            User user = foundUser.get();
            UserDetails logInfos = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(getGrantedAuthorities(user.getRole()))
                    .build();
            return logInfos;
        } else {
            throw new UserNotFoundException(username);
        }
    }

    /**
     * Génère les autorisations associées au rôle d'un utilisateur.
     * 
     * @param role Le rôle de l'utilisateur ("USER", "ADMIN").
     * @return Une liste d'autorisations {@link GrantedAuthority} basées sur le
     *         rôle.
     */
    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        if (role.equals("ADMIN"))
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}