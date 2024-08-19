package com.nnk.springboot.integration;

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

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attribute("ratings", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("moodysRating", "Moodys Rating")
				.param("sandPRating", "Sand PRating")
				.param("fitchRating", "Fitch Rating")
				.param("orderNumber", "10"))
				.andExpect(status().isCreated());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attribute("ratings", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rating"))
				.andExpect(model().attribute("rating", hasProperty("orderNumber", is(10))));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.put("/rating/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("moodysRating", "Moodys Rating")
				.param("sandPRating", "Sand PRating")
				.param("fitchRating", "Fitch Rating")
				.param("orderNumber", "20"))
				.andExpect(status().isOk());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rating"))
				.andExpect(model().attribute("rating", hasProperty("orderNumber", is(20))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/rating/delete/1"))
				.andExpect(status().isNoContent());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/rating/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("ratings"))
				.andExpect(model().attribute("ratings", hasSize(0)));
	}

	@Test
	public void userErroredTest() throws Exception {

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/rating/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("rating", "moodysRating"))
				.andExpect(model().attributeHasFieldErrors("rating", "sandPRating"))
				.andExpect(model().attributeHasFieldErrors("rating", "fitchRating"))
				.andExpect(model().attributeHasFieldErrors("rating", "orderNumber"));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.put("/rating/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("rating", "moodysRating"))
				.andExpect(model().attributeHasFieldErrors("rating", "sandPRating"))
				.andExpect(model().attributeHasFieldErrors("rating", "fitchRating"))
				.andExpect(model().attributeHasFieldErrors("rating", "orderNumber"));

		// Find
		mockMvc.perform(MockMvcRequestBuilders.get("/rating/update/1"))
				.andExpect(status().isNotFound());

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/rating/delete/1"))
				.andExpect(status().isNotFound());
	}
}
