package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.model.User;
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
public class UserTests {

	private final MockMvc mockMvc;

	public UserTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void userTest() throws Exception {
		User user = new User("Username", "Password", "Fullname", "User");
		String userAsJson = new ObjectMapper().writeValueAsString(user);

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/user/validate").content(
				userAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", hasProperty("username", is("Username"))))
				.andExpect(model().attribute("user", hasProperty("password", is(""))));

		// Update
		user.setUsername("Username Updated");
		userAsJson = new ObjectMapper().writeValueAsString(user);
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1").content(
				userAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", hasProperty("username", is("Username Updated"))))
				.andExpect(model().attribute("user", hasProperty("password", is(""))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/user/delete/1").content(
				userAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users", hasSize(0)));
	}
}
