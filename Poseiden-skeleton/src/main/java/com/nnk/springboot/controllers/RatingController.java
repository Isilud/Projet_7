package com.nnk.springboot.controllers;

import org.springframework.http.HttpStatus;
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

import com.nnk.springboot.exceptions.RatingNotFoundException;
import com.nnk.springboot.exceptions.RatingValidationException;
import com.nnk.springboot.model.Rating;
import com.nnk.springboot.services.RatingService;

import jakarta.validation.Valid;

@Controller
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @RequestMapping("/rating/list")
    @ResponseStatus(HttpStatus.OK)
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.getAllRating());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    @ResponseStatus(HttpStatus.OK)
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    @ResponseStatus(HttpStatus.CREATED)
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            ratingService.saveRating(rating);
            model.addAttribute("ratings", ratingService.getAllRating());
            return "redirect:/rating/list";
        }
        throw new RatingValidationException("rating/update", result);
    }

    @GetMapping("/rating/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.getRating(id)
                .orElseThrow(() -> new RatingNotFoundException(id));
        model.addAttribute("rating", rating);
        return "rating/update";
    }

    @PutMapping("/rating/update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateBid(@PathVariable("id") Integer id, @Valid Rating rating,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            throw new RatingValidationException("rating/update", result);
        }

        rating.setId(id);
        ratingService.saveRating(rating);
        model.addAttribute("ratings", ratingService.getAllRating());
        return "redirect:/rating/list";
    }

    @DeleteMapping("/rating/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        Rating rating = ratingService.getRating(id)
                .orElseThrow(() -> new RatingNotFoundException(id));
        ratingService.deleteRating(rating);
        model.addAttribute("ratings", ratingService.getAllRating());
        return "redirect:/rating/list";
    }
}
