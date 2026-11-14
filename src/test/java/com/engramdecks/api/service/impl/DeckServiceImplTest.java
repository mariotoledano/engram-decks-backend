package com.engramdecks.api.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.engramdecks.api.dto.request.PatchDeckRequest;
import com.engramdecks.api.mapper.DeckMapper;
import com.engramdecks.api.repository.DeckRepository;
import com.engramdecks.api.repository.FlashcardRepository;

@ExtendWith(MockitoExtension.class)
class DeckServiceImplTest {

	@Mock
	private DeckRepository deckRepository;

	@Mock
	private FlashcardRepository flashcardRepository;

	@Mock
	private DeckMapper deckMapper;

	@InjectMocks
	private DeckServiceImpl deckService;

	@Test
	void patchThrowsWhenNameIsBlank() {
		PatchDeckRequest request = new PatchDeckRequest("   ", null, null);

		assertThrows(IllegalArgumentException.class, () -> deckService.patch("deck-1", request));

		verify(deckRepository, never()).save(any());
	}

}
