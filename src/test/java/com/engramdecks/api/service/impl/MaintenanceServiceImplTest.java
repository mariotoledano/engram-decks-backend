package com.engramdecks.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.engramdecks.api.mapper.FlashcardMapper;
import com.engramdecks.api.repository.DeckRepository;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.DeckService;

@ExtendWith(MockitoExtension.class)
class MaintenanceServiceImplTest {

	@Mock
	private DeckRepository deckRepository;

	@Mock
	private FlashcardRepository flashcardRepository;

	@Mock
	private FlashcardSearch flashcardSearch;

	@Mock
	private DeckService deckService;

	private MaintenanceServiceImpl maintenanceService;

	@BeforeEach
	void setUp() {
		maintenanceService = new MaintenanceServiceImpl(
				deckRepository,
				flashcardRepository,
				flashcardSearch,
				new FlashcardMapper(),
				deckService);
	}

	@Test
	void wipeRejectsAWrongConfirmation() {
		assertThrows(IllegalArgumentException.class, () -> maintenanceService.deleteAllDecks("no"));
		assertThrows(IllegalArgumentException.class, () -> maintenanceService.deleteAllFlashcards("borrar"));
		assertThrows(IllegalArgumentException.class, () -> maintenanceService.deleteUnlinked("no"));

		verify(deckService, never()).deleteAll();
		verify(flashcardRepository, never()).deleteAll();
		verify(flashcardRepository, never()).deleteUnlinked();
	}

	@Test
	void summaryCountsUnlinkedWithoutLoadingThem() {
		when(deckRepository.count()).thenReturn(1L);
		when(flashcardRepository.count()).thenReturn(4L);
		when(flashcardRepository.countUnlinked()).thenReturn(2L);
		when(flashcardSearch.distinctTags()).thenReturn(List.of("java"));

		var summary = maintenanceService.summary();

		assertEquals(1L, summary.deckCount());
		assertEquals(4L, summary.flashcardCount());
		assertEquals(2L, summary.unlinkedCount());
		assertEquals(1L, summary.tagCount());
		verify(flashcardRepository, never()).findUnlinked();
	}

	@Test
	void deleteAllDecksUnlinksThroughDeckService() {
		maintenanceService.deleteAllDecks("BORRAR");

		verify(deckService).deleteAll();
	}

	@Test
	void deleteAllFlashcardsClearsTheCollection() {
		maintenanceService.deleteAllFlashcards("BORRAR");

		verify(flashcardRepository).deleteAll();
	}

}
