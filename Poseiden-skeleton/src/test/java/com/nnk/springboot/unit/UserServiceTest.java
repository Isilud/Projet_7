package com.nnk.springboot.unit;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.nnk.springboot.model.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    public UserService userService;

    User defaultUser;

    @BeforeEach
    public void setup() {
        defaultUser = new User("Username", "Password$10", "Fullname", "User");
        userService = new UserService(userRepository);
    }

    @Test
    public void getAllUserTest() {
        List<User> allUser = new ArrayList<User>();
        allUser.add(defaultUser);

        when(userRepository.findAll()).thenReturn(allUser);
        userService.getAllUser();

        verify(userRepository).findAll();
    }

    @Test
    public void getUserTest() {
        when(userRepository.findById(1)).thenReturn(Optional.of(defaultUser));
        userService.getUser(1);

        verify(userRepository).findById(1);
    }

    @Test
    public void saveUserTest() {
        userService.saveUser(defaultUser);

        verify(userRepository).save(defaultUser);
    }

    @Test
    public void deleteUserTest() {
        userService.deleteUser(defaultUser);

        verify(userRepository).delete(defaultUser);
    }

}
