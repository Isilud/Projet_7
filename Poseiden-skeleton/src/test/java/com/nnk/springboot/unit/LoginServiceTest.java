package com.nnk.springboot.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import com.nnk.springboot.model.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.LoginService;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private UserRepository userRepository;

    public LoginService loginService;

    User defaultUser;

    @BeforeEach
    public void setup() {
        defaultUser = new User("Username", "Password", "Fullname", "USER");
        loginService = new LoginService(userRepository);
    }

    @Test
    public void loadUserByUsernameTest() {
        when(userRepository.findByUsername("Username")).thenReturn(Optional.of(defaultUser));

        UserDetails userDetails = loginService.loadUserByUsername("Username");

        assertNotNull(userDetails);
        assertEquals(defaultUser.getUsername(), userDetails.getUsername());
        assertEquals(defaultUser.getPassword(), userDetails.getPassword());
    }

    @Test
    public void getUserAuthorityTest() {
        when(userRepository.findByUsername(defaultUser.getUsername())).thenReturn(Optional.of(defaultUser));

        UserDetails userDetails = loginService.loadUserByUsername(defaultUser.getUsername());

        assertFalse(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    public void getAdminAuthorityTest() {
        defaultUser.setRole("ADMIN");
        when(userRepository.findByUsername(defaultUser.getUsername())).thenReturn(Optional.of(defaultUser));

        // Act
        UserDetails userDetails = loginService.loadUserByUsername(defaultUser.getUsername());

        // Assert
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }
}
