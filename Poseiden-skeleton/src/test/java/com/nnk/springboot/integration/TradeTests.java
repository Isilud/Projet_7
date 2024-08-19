package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.model.Trade;
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
		Trade trade = new Trade("Trade Account", "Type");
		String tradeAsJson = new ObjectMapper().writeValueAsString(trade);

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trades"))
				.andExpect(model().attribute("trades", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/trade/validate").content(
				tradeAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

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
		trade.setAccount("Trade Account Update");
		tradeAsJson = new ObjectMapper().writeValueAsString(trade);
		mockMvc.perform(MockMvcRequestBuilders.post("/trade/update/1").content(
				tradeAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trade"))
				.andExpect(model().attribute("trade", hasProperty("account", is("Trade Account Update"))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/trade/delete/1").content(
				tradeAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/trade/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("trades"))
				.andExpect(model().attribute("trades", hasSize(0)));
	}
}
