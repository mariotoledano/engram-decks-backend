package com.engramdecks.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.PreviewStudySessionRequest;
import com.engramdecks.api.dto.request.SaveStudySessionRequest;
import com.engramdecks.api.dto.request.TagMatch;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.dto.response.StudySessionPreviewResponse;
import com.engramdecks.api.exception.ResourceNotFoundException;
import com.engramdecks.api.mapper.FlashcardMapper;
import com.engramdecks.api.model.Flashcard;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.DeckService;

@ExtendWith(MockitoExtension.class)
class StudySessionServiceImplTest {

	@Mock
	private FlashcardSearch flashcardSearch;

	@Mock
	private FlashcardRepository flashcardRepository;

	@Mock
	private DeckService deckService;

	private StudySessionServiceImpl studySessionService;

	@BeforeEach
	void setUp() {
		studySessionService = new StudySessionServiceImpl(
				flashcardSearch,
				flashcardRepository,
				new FlashcardMapper(),
				deckService);
	}

	@Test
	void previewLimitsWithoutPersisting() {
		when(flashcardSearch.find(Set.of("java"), TagMatch.ANY, List.of(), null)).thenReturn(List.of(
				flashcard("a"),
				flashcard("b"),
				flashcard("c")));

		StudySessionPreviewResponse result = studySessionService.preview(new PreviewStudySessionRequest(
				Set.of(" Java "),
				TagMatch.ANY,
				2,
				false,
				null,
				null));

		assertEquals(3, result.totalMatched());
		assertEquals(List.of("a", "b"), result.cards().stream().map(card -> card.id()).toList());
		verify(deckService, never()).create(any());
		verify(flashcardRepository, never()).save(any());
	}

	@Test
	void previewPassesAllMatch() {
		when(flashcardSearch.find(Set.of("java", "spring"), TagMatch.ALL, List.of("deck-1"), "spring")).thenReturn(List.of());

		StudySessionPreviewResponse result = studySessionService.preview(new PreviewStudySessionRequest(
				Set.of("Spring", "java"),
				TagMatch.ALL,
				10,
				false,
				List.of("deck-1"),
				"spring"));

		assertEquals(0, result.totalMatched());
		assertEquals(List.of(), result.cards());
	}

	@Test
	void saveLinksExistingFlashcardsAndDoesNotCopyThem() {
		Flashcard flashcard = flashcard("card-1");
		flashcard.setDeckIds(new HashSet<>(Set.of("old")));
		when(flashcardRepository.findById("card-1")).thenReturn(Optional.of(flashcard));
		when(deckService.create(new CreateDeckRequest("Repaso", null, Set.of("Java"), null)))
				.thenReturn(new DeckResponse("deck-new", "Repaso", null, Set.of("java"), null, null, null, null));

		var saved = studySessionService.save(new SaveStudySessionRequest(
				"Repaso",
				null,
				Set.of("Java"),
				List.of("card-1", "card-1")));

		assertEquals("deck-new", saved.id());
		assertEquals("card-1", flashcard.getId());
		assertEquals(Set.of("old", "deck-new"), flashcard.getDeckIds());
		verify(flashcardRepository, times(1)).save(flashcard);
		verify(flashcardRepository, never()).deleteById(any());
	}

	@Test
	void saveDoesNotCreateADeckWhenAFlashcardIsMissing() {
		when(flashcardRepository.findById("card-1")).thenReturn(Optional.of(flashcard("card-1")));
		when(flashcardRepository.findById("missing")).thenReturn(Optional.empty());

		assertThrows(
				ResourceNotFoundException.class,
				() -> studySessionService.save(new SaveStudySessionRequest(
						"Repaso",
						null,
						Set.of(),
						List.of("card-1", "missing"))));

		verify(deckService, never()).create(any());
		verify(flashcardRepository, never()).save(any());
	}

	private static Flashcard flashcard(String id) {
		return Flashcard.builder()
				.id(id)
				.front("front")
				.back("back")
				.build();
	}

}
