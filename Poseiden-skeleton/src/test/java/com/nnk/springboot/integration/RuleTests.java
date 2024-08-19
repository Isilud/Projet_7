package com.nnk.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nnk.springboot.model.RuleName;
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
		RuleName rule = new RuleName("Rule Name", "Description", "Json", "Template", "SQL", "SQL Part");
		String ruleAsJson = new ObjectMapper().writeValueAsString(rule);

		// Initial state
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rules"))
				.andExpect(model().attribute("rules", hasSize(0)));

		// Save
		mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/validate").content(
				ruleAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rules"))
				.andExpect(model().attribute("rules", hasSize(1)));

		// Find
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/find/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rule"))
				.andExpect(model().attribute("rule", hasProperty("name", is("Rule Name"))));

		// Update
		rule.setName("Rule Name Update");
		ruleAsJson = new ObjectMapper().writeValueAsString(rule);
		mockMvc.perform(MockMvcRequestBuilders.post("/ruleName/update/1").content(
				ruleAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/find/1"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rule"))
				.andExpect(model().attribute("rule", hasProperty("name", is("Rule Name Update"))));

		// Delete
		mockMvc.perform(MockMvcRequestBuilders.delete("/ruleName/delete/1").content(
				ruleAsJson)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isFound());

		// Final State
		mockMvc.perform(
				MockMvcRequestBuilders.get("/ruleName/list"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("rules"))
				.andExpect(model().attribute("rules", hasSize(0)));
	}
}
