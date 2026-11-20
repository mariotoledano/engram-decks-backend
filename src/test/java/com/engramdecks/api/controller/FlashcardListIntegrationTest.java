package com.engramdecks.api.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import com.engramdecks.api.model.Flashcard;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.FlashcardService;

@SpringBootTest
@AutoConfigureMockMvc
class FlashcardListIntegrationTest {

	private static final String SEED_FRONT = "integration-list-front";
	private static final String SEED_BACK = "integration-list-back";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private FlashcardService flashcardService;

	@Autowired
	private FlashcardRepository flashcardRepository;

	private String seededCardId;

	@BeforeEach
	void seedFlashcard() {
		Flashcard saved = flashcardRepository.save(Flashcard.builder()
				.front(SEED_FRONT)
				.back(SEED_BACK)
				.build());
		seededCardId = saved.getId();
	}

	@AfterEach
	void removeSeededFlashcard() {
		if (seededCardId != null) {
			flashcardRepository.deleteById(seededCardId);
			seededCardId = null;
		}
	}

	@Test
	void findAllReturnsMappedFlashcards() {
		assertThat(flashcardService.findAll()).anySatisfy(card -> {
			assertThat(card.id()).isEqualTo(seededCardId);
			assertThat(card.front()).isEqualTo(SEED_FRONT);
			assertThat(card.back()).isEqualTo(SEED_BACK);
		});
	}

	@Test
	void listAllFlashcardsEndpointReturnsOk() throws Exception {
		mockMvc.perform(get("/api/v1/flashcards"))
				.andExpect(status().isOk());
	}

	@Test
	void unlinkedFlashcardsEndpointReturnsOk() throws Exception {
		mockMvc.perform(get("/api/v1/flashcards/unlinked"))
				.andExpect(status().isOk());
	}

}