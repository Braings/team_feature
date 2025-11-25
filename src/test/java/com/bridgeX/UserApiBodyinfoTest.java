package com.bridgeX;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

// import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional
public class UserApiBodyinfoTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@WithMockUser(username = "Potato", roles = "ADMIN")
	void updateAndGetBodyInfo_forPotato() throws Exception {

		String requestJson = """
				{
					"height": 168,
					"weight": 55,
					"gender": "MALE"
				}""";

		mockMvc.perform(put("/api/me/body")
				.with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
		.andExpect(status().isOk());
		
		mockMvc.perform(get("/api/me/body"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.height").value(168))
		.andExpect(jsonPath("$.weight").value(55))
		.andExpect(jsonPath("$.gender").value("MALE"));
		}
}
