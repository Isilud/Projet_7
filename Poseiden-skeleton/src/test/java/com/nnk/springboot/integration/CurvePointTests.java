package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.model.CurvePoint;
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
		CurvePoint curve = new CurvePoint(10, 10d, 30d);
		String curveAsJson = new ObjectMapper().writeValueAsString(curve);

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curves"))
				.andExpect(model().attribute("curves", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/validate").content(
				curveAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curves"))
				.andExpect(model().attribute("curves", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curve"))
				.andExpect(model().attribute("curve", hasProperty("curveId", is(10))));

		// Update
		curve.setCurveId(20);
		curveAsJson = new ObjectMapper().writeValueAsString(curve);
		mockMvc.perform(MockMvcRequestBuilders.post("/curvePoint/update/1").content(
				curveAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curve"))
				.andExpect(model().attribute("curve", hasProperty("curveId", is(20))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/curvePoint/delete/1").content(
				curveAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/curvePoint/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("curves"))
				.andExpect(model().attribute("curves", hasSize(0)));
	}

}
