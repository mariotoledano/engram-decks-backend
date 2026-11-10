package com.engramdecks.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.exception.ResourceNotFoundException;
import com.engramdecks.api.mapper.FlashcardMapper;
import com.engramdecks.api.model.Flashcard;
import com.engramdecks.api.repository.DeckRepository;
import com.engramdecks.api.repository.FlashcardRepository;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceImplTest {

	@Mock
	private FlashcardRepository flashcardRepository;

	@Mock
	private DeckRepository deckRepository;

	@Mock
	private FlashcardMapper flashcardMapper;

	@InjectMocks
	private FlashcardServiceImpl flashcardService;

	@Test
	void createReturnsFlashcardWhenDeckExists() {
		CreateFlashcardRequest request = new CreateFlashcardRequest("front", "back");
		Flashcard toSave = Flashcard.builder().deckId("deck-1").front("front").back("back").build();
		Flashcard saved = Flashcard.builder().id("card-1").deckId("deck-1").front("front").back("back").build();
		FlashcardResponse response = new FlashcardResponse("card-1", "deck-1", "front", "back", null, null);

		when(deckRepository.existsById("deck-1")).thenReturn(true);
		when(flashcardMapper.toEntity("deck-1", request)).thenReturn(toSave);
		when(flashcardRepository.save(toSave)).thenReturn(saved);
		when(flashcardMapper.toResponse(saved)).thenReturn(response);

		FlashcardResponse result = flashcardService.create("deck-1", request);

		assertEquals(response, result);
		verify(flashcardRepository).save(toSave);
	}

	@Test
	void createThrowsWhenDeckDoesNotExist() {
		CreateFlashcardRequest request = new CreateFlashcardRequest("front", "back");
		when(deckRepository.existsById("missing")).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> flashcardService.create("missing", request));

		verify(flashcardRepository, never()).save(any());
	}

}
