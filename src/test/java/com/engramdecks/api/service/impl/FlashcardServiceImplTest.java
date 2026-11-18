package com.engramdecks.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.request.UpdateFlashcardRequest;
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

	private FlashcardServiceImpl flashcardService;

	@BeforeEach
	void setUp() {
		flashcardService = new FlashcardServiceImpl(flashcardRepository, deckRepository, new FlashcardMapper());
	}

	@Test
	void createReturnsFlashcardWhenDeckExists() {
		CreateFlashcardRequest request = new CreateFlashcardRequest("front", "back");
		when(deckRepository.existsById("deck-1")).thenReturn(true);
		when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> {
			Flashcard flashcard = invocation.getArgument(0);
			flashcard.setId("card-1");
			return flashcard;
		});

		FlashcardResponse result = flashcardService.create("deck-1", request);

		assertEquals("card-1", result.id());
		assertEquals(List.of("deck-1"), result.deckIds());
		assertEquals("front", result.front());
		assertEquals("back", result.back());
	}

	@Test
	void createThrowsWhenDeckDoesNotExist() {
		CreateFlashcardRequest request = new CreateFlashcardRequest("front", "back");
		when(deckRepository.existsById("missing")).thenReturn(false);

		assertThrows(ResourceNotFoundException.class, () -> flashcardService.create("missing", request));

		verify(flashcardRepository, never()).save(any());
	}

	@Test
	void updateKeepsSeveralDecksAndDropsDuplicates() {
		Flashcard existing = flashcard("card-1", Set.of("deck-1"));
		when(flashcardRepository.findById("card-1")).thenReturn(Optional.of(existing));
		when(deckRepository.existsById("deck-2")).thenReturn(true);
		when(deckRepository.existsById("deck-1")).thenReturn(true);
		when(flashcardRepository.save(existing)).thenReturn(existing);

		FlashcardResponse result = flashcardService.update(
				"card-1",
				new UpdateFlashcardRequest("front", "back", List.of("deck-2", "deck-1", "deck-1")));

		assertEquals(List.of("deck-2", "deck-1"), result.deckIds());
		assertEquals(Set.of("deck-2", "deck-1"), existing.getDeckIds());
	}

	@Test
	void updateAllowsAnEmptyDeckList() {
		Flashcard existing = flashcard("card-1", Set.of("deck-1"));
		when(flashcardRepository.findById("card-1")).thenReturn(Optional.of(existing));
		when(flashcardRepository.save(existing)).thenReturn(existing);

		FlashcardResponse result = flashcardService.update(
				"card-1",
				new UpdateFlashcardRequest("front", "back", List.of()));

		assertEquals(List.of(), result.deckIds());
		assertEquals(Set.of(), existing.getDeckIds());
		verify(deckRepository, never()).existsById(any());
	}

	@Test
	void updateRejectsAnUnknownDeck() {
		Flashcard existing = flashcard("card-1", Set.of());
		when(flashcardRepository.findById("card-1")).thenReturn(Optional.of(existing));
		when(deckRepository.existsById("missing")).thenReturn(false);

		assertThrows(
				IllegalArgumentException.class,
				() -> flashcardService.update("card-1", new UpdateFlashcardRequest("front", "back", List.of("missing"))));

		verify(flashcardRepository, never()).save(any());
	}

	@Test
	void findUnlinkedEscapesTheQueryAndReturnsCardsWithoutDecks() {
		Flashcard card = flashcard("card-1", Set.of());
		card.setFront("a+b");
		when(flashcardRepository.findUnlinkedMatching("a\\+b")).thenReturn(List.of(card));

		List<FlashcardResponse> result = flashcardService.findUnlinked(" a+b ");

		assertEquals(1, result.size());
		assertEquals(List.of(), result.getFirst().deckIds());
		assertEquals("a+b", result.getFirst().front());
		verify(flashcardRepository, never()).findUnlinked();
	}

	@Test
	void findUnlinkedWithoutQueryReturnsEveryUnlinkedCard() {
		when(flashcardRepository.findUnlinked()).thenReturn(List.of(flashcard("card-1", Set.of())));

		List<FlashcardResponse> result = flashcardService.findUnlinked("   ");

		assertEquals(1, result.size());
		verify(flashcardRepository).findUnlinked();
		verify(flashcardRepository, never()).findUnlinkedMatching(any());
	}

	@Test
	void createNormalizesTags() {
		CreateFlashcardRequest request = new CreateFlashcardRequest("front", "back", null, Set.of(" Java ", "java"));
		when(deckRepository.existsById("deck-1")).thenReturn(true);
		when(flashcardRepository.save(any(Flashcard.class))).thenAnswer(invocation -> {
			Flashcard flashcard = invocation.getArgument(0);
			flashcard.setId("card-1");
			return flashcard;
		});

		FlashcardResponse result = flashcardService.create("deck-1", request);

		assertEquals(List.of("java"), result.tags());
	}

	private static Flashcard flashcard(String id, Set<String> deckIds) {
		return Flashcard.builder()
				.id(id)
				.deckIds(new HashSet<>(deckIds))
				.front("front")
				.back("back")
				.build();
	}

}
