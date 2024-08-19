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

import com.nnk.springboot.model.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingService;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    public RatingService ratingService;

    Rating defaultRating;

    @BeforeEach
    public void setup() {
        defaultRating = new Rating("moodys", "sandP", "fitch", 1);
        ratingService = new RatingService(ratingRepository);
    }

    @Test
    public void getAllRatingTest() {
        List<Rating> allRating = new ArrayList<Rating>();
        allRating.add(defaultRating);

        when(ratingRepository.findAll()).thenReturn(allRating);
        ratingService.getAllRating();

        verify(ratingRepository).findAll();
    }

    @Test
    public void getRatingTest() {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(defaultRating));
        ratingService.getRating(1);

        verify(ratingRepository).findById(1);
    }

    @Test
    public void saveRatingTest() {
        ratingService.saveRating(defaultRating);

        verify(ratingRepository).save(defaultRating);
    }

    @Test
    public void deleteRatingTest() {
        ratingService.deleteRating(defaultRating);

        verify(ratingRepository).delete(defaultRating);
    }

}
