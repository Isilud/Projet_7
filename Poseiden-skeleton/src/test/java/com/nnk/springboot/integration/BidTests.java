package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.model.BidList;

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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class BidTests {

	private final MockMvc mockMvc;

	public BidTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void bidListTest() throws Exception {
		BidList bid = new BidList("Account Test", "Type Test", 10d);
		String bidAsJson = new ObjectMapper().writeValueAsString(bid);

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/bidList/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("bids"))
				.andExpect(model().attribute("bids", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/bidList/validate").content(
				bidAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/bidList/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("bids"))
				.andExpect(model().attribute("bids", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/bidList/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("bid"))
				.andExpect(model().attribute("bid", hasProperty("bidQuantity", is(10d))));

		// Update
		bid.setBidQuantity(20d);
		bidAsJson = new ObjectMapper().writeValueAsString(bid);
		mockMvc.perform(MockMvcRequestBuilders.post("/bidList/update/1").content(
				bidAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/bidList/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("bid"))
				.andExpect(model().attribute("bid", hasProperty("bidQuantity", is(20d))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/bidList/delete/1").content(
				bidAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/bidList/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("bids"))
				.andExpect(model().attribute("bids", hasSize(0)));

	}
}
