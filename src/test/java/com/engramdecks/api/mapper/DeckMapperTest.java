package com.engramdecks.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.model.Deck;
import com.engramdecks.api.model.DeckCover;
import com.engramdecks.api.model.DeckCoverType;

class DeckMapperTest {

	private final DeckMapper mapper = new DeckMapper();

	@Test
	void createWithoutCoverUsesTheDefaultColor() {
		Deck deck = mapper.toEntity(new CreateDeckRequest("Filosofía", null, null, null));

		assertEquals(DeckCoverType.COLOR, deck.getCover().getType());
		assertEquals(DeckCover.DEFAULT_COLOR, deck.getCover().getValue());
	}

	@Test
	void updateKeepsAnExistingCoverWhenTheRequestOmitsIt() {
		Deck deck = Deck.builder()
				.name("Old")
				.cover(new DeckCover(DeckCoverType.COLOR, "#112233"))
				.build();

		mapper.update(deck, new UpdateDeckRequest("New", null, null, null));

		assertEquals("New", deck.getName());
		assertEquals(DeckCoverType.COLOR, deck.getCover().getType());
		assertEquals("#112233", deck.getCover().getValue());
	}

}
