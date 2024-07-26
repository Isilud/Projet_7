package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    public void saveRating(Rating rating) {
        ratingRepository.save(rating);
    }

    public void deleteRating(Rating rating) {
        ratingRepository.delete(rating);
    }

    public Optional<Rating> getRating(Integer id) {
        return ratingRepository.findById(id);
    }

}
