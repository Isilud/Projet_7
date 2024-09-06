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
public class RuleTests {

	private final MockMvc mockMvc;

	public RuleTests(@Autowired MockMvc mockMvc) {
		this.mockMvc = mockMvc;
	}

	@Test
	public void ruleTest() throws Exception {

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rules"))
				.andExpect(model().attribute("rules", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Rule Name")
				.param("description", "Description")
				.param("json", "Json")
				.param("template", "Template")
				.param("sqlStr", "SQL")
				.param("sqlPart", "SQL Part"))
				.andExpect(status().isCreated());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rules"))
				.andExpect(model().attribute("rules", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rule"))
				.andExpect(model().attribute("rule", hasProperty("name", is("Rule Name"))));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("name", "Rule Name Update")
				.param("description", "Description")
				.param("json", "Json")
				.param("template", "Template")
				.param("sqlStr", "SQL")
				.param("sqlPart", "SQL Part"))
				.andExpect(status().isOk());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/update/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rule"))
				.andExpect(model().attribute("rule", hasProperty("name", is("Rule Name Update"))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"))
				.andExpect(status().isNoContent());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rules"))
				.andExpect(model().attribute("rules", hasSize(0)));
	}

	@Test
	public void ruleErroredTest() throws Exception {

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("ruleName", "name"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "description"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "json"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "template"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "sqlPart"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "sqlPart"));

		// Update
		mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED))
				.andExpect(status().isBadRequest())
				.andExpect(model().attributeHasFieldErrors("ruleName", "name"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "description"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "json"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "template"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "sqlPart"))
				.andExpect(model().attributeHasFieldErrors("ruleName", "sqlPart"));

		// Find
		mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/update/1"))
				.andExpect(status().isNotFound());

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.get("/ruleName/delete/1"))
				.andExpect(status().isNotFound());
	}
}
