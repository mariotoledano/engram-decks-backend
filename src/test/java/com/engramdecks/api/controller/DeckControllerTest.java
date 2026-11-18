package com.engramdecks.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.engramdecks.api.exception.GlobalExceptionHandler;
import com.engramdecks.api.service.DeckService;

@WebMvcTest(DeckController.class)
@Import(GlobalExceptionHandler.class)
class DeckControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private DeckService deckService;

	@Test
	void malformedJsonIsBadRequest() throws Exception {
		mockMvc.perform(post("/api/v1/decks")
						.contentType(MediaType.APPLICATION_JSON)
						.content("{"))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.message").value("Malformed request body"))
				.andExpect(jsonPath("$.details").isArray());
	}

}
