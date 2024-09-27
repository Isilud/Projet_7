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
public class CurvePointTests {

	private final MockMvc mockMvc;

	public CurvePointTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void curvePointTest() throws Exception {

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curvePoints"))
				.andExpect(model().attribute("curvePoints", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("curveId", "10")
				.param("term", "10")
				.param("amount", "30"))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curvePoints"))
				.andExpect(model().attribute("curvePoints", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curvePoint"))
				.andExpect(model().attribute("curvePoint", hasProperty("curveId", is(10))));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("curveId", "20")
				.param("term", "10.")
				.param("amount", "30."))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curvePoint"))
				.andExpect(model().attribute("curvePoint", hasProperty("curveId", is(20))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curvePoints"))
				.andExpect(model().attribute("curvePoints", hasSize(0)));
	}

	@Test
	public void curveErroredTest() throws Exception {

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"))
				.andExpect(model().attributeHasFieldErrors("curvePoint", "term"))
				.andExpect(model().attributeHasFieldErrors("curvePoint", "amount"));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("curvePoint", "curveId"))
				.andExpect(model().attributeHasFieldErrors("curvePoint", "term"))
				.andExpect(model().attributeHasFieldErrors("curvePoint", "amount"));

		// Find
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/update/1"))
				.andExpect(status().isNotFound());

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.get("/curvePoint/delete/1"))
				.andExpect(status().isNotFound());
	}

}
