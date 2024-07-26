package com.nnk.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;

@Service
public class RatingService {

    private final RatingRepository RatingRepository;

    public RatingService(RatingRepository RatingRepository) {
        this.RatingRepository = RatingRepository;
    }

    public List<Rating> getAllRating() {
        return RatingRepository.findAll();
    }

    public void saveRating(Rating rating) {
        RatingRepository.save(rating);
    }

    public void deleteRating(Rating rating) {
        RatingRepository.delete(rating);
    }

    public Optional<Rating> getRating(Integer id) {
        return RatingRepository.findById(id);
    }

}
