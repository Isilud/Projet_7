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
public class TradeTests {

	private final MockMvc mockMvc;

	public TradeTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void tradeTest() throws Exception {

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trades"))
				.andExpect(model().attribute("trades", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("account", "Trade Account")
				.param("type", "Type"))
				.andExpect(status().isCreated());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trades"))
				.andExpect(model().attribute("trades", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trade"))
				.andExpect(model().attribute("trade", hasProperty("account", is("Trade Account"))));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.put("/trade/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("account", "Trade Account Update")
				.param("type", "Type"))
				.andExpect(status().isOk());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trade"))
				.andExpect(model().attribute("trade", hasProperty("account", is("Trade Account Update"))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/trade/delete/1"))
				.andExpect(status().isNoContent());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trades"))
				.andExpect(model().attribute("trades", hasSize(0)));
	}

	@Test
	public void tradeErroredTest() throws Exception {

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("trade", "account"))
				.andExpect(model().attributeHasFieldErrors("trade", "type"));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.put("/trade/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("trade", "account"))
				.andExpect(model().attributeHasFieldErrors("trade", "type"));

		// Find
		mockMvc.perform(MockMvcRequestBuilders.get("/trade/update/1"))
				.andExpect(status().isNotFound());

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/trade/delete/1"))
				.andExpect(status().isNotFound());
	}
}
