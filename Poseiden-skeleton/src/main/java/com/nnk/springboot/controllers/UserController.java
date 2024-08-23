package com.nnk.springboot.controllers;

import com.nnk.springboot.exceptions.UserNotFoundException;
import com.nnk.springboot.exceptions.UserValidationException;
import com.nnk.springboot.model.User;
import com.nnk.springboot.services.UserService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/user/list")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "user/list";
    }

    @GetMapping("/user/add")
    @ResponseStatus(HttpStatus.OK)
    public String addUser(User user) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public String validate(@Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
            userService.saveUser(user);
            model.addAttribute("users", userService.getAllUser());
            return "redirect:/user/list";
        }
        throw new UserValidationException("user/add", result);
    }

    @GetMapping("/user/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        user.setPassword("");
        model.addAttribute("user", user);
        return "user/update";
    }

    @PutMapping("/user/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateUser(@PathVariable("id") Integer id,
            @Valid User user,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            throw new UserValidationException("user/update", result);
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        user.setId(id);
        userService.saveUser(user);
        model.addAttribute("users", userService.getAllUser());
        return "redirect:/user/list";
    }

    @DeleteMapping("/user/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteUser(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUser(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userService.deleteUser(user);
        model.addAttribute("users", userService.getAllUser());
        return "redirect:/user/list";
    }
}
