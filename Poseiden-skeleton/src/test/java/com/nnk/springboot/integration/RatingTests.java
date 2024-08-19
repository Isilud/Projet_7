package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.model.Rating;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class RatingTests {

	private final MockMvc mockMvc;

	public RatingTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void ratingTest() throws Exception {
		Rating rating = new Rating("Moodys Rating", "Sand PRating", "Fitch Rating", 10);
		String ratingAsJson = new ObjectMapper().writeValueAsString(rating);

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attribute("ratings", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate").content(
				ratingAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attribute("ratings", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/find/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rating"))
				.andExpect(model().attribute("rating", hasProperty("orderNumber", is(10))));

		// Update
		rating.setOrderNumber(20);
		ratingAsJson = new ObjectMapper().writeValueAsString(rating);
		mockMvc.perform(MockMvcRequestBuilders.post("/rating/update/1").content(
				ratingAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/find/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rating"))
				.andExpect(model().attribute("rating", hasProperty("orderNumber", is(20))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/rating/delete/1").content(
				ratingAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attribute("ratings", hasSize(0)));
	}
}
