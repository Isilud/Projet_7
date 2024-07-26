package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nnk.springboot.model.User;
import com.nnk.springboot.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public void saveUser(User bid) {
        userRepository.save(bid);
    }

    public void deleteUser(User bid) {
        userRepository.delete(bid);
    }

    public Optional<User> getUser(Integer id) {
        return userRepository.findById(id);
    }

}
