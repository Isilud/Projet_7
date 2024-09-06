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
public class UserTests {

	private final MockMvc mockMvc;

	public UserTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void userTest() throws Exception {

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "Username")
				.param("password", "Password")
				.param("fullname", "Fullname")
				.param("role", "USER"))
				.andExpect(status().isCreated());

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
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "Username Updated")
				.param("password", "Password")
				.param("fullname", "Fullname")
				.param("role", "USER"))
				.andExpect(status().isOk());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("user"))
				.andExpect(model().attribute("user", hasProperty("username", is("Username Updated"))))
				.andExpect(model().attribute("user", hasProperty("password", is(""))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1"))
				.andExpect(status().isNoContent());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/user/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("users"))
				.andExpect(model().attribute("users", hasSize(0)));
	}

	@Test
	public void userErroredTest() throws Exception {

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/user/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("user", "username"))
				.andExpect(model().attributeHasFieldErrors("user", "password"))
				.andExpect(model().attributeHasFieldErrors("user", "fullname"))
				.andExpect(model().attributeHasFieldErrors("user", "role"));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.post("/user/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("user", "username"))
				.andExpect(model().attributeHasFieldErrors("user", "password"))
				.andExpect(model().attributeHasFieldErrors("user", "fullname"))
				.andExpect(model().attributeHasFieldErrors("user", "role"));

		// Find
		mockMvc.perform(MockMvcRequestBuilders.get("/user/update/1"))
				.andExpect(status().isNotFound());

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.get("/user/delete/1"))
				.andExpect(status().isNotFound());
	}
}
