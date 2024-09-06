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

@Service
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

    private List<GrantedAuthority> getGrantedAuthorities(String role) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        if (role.equals("ADMIN"))
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return authorities;
    }
}