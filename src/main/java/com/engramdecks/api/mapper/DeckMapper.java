package com.engramdecks.api.mapper;

import org.springframework.stereotype.Component;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.model.Deck;

@Component
public class DeckMapper {

	public Deck toEntity(CreateDeckRequest request) {
		return Deck.builder()
				.name(request.name())
				.description(request.description())
				.build();
	}

	public void update(Deck deck, UpdateDeckRequest request) {
		deck.setName(request.name());
		deck.setDescription(request.description());
	}

	public DeckResponse toResponse(Deck deck) {
		return new DeckResponse(
				deck.getId(),
				deck.getName(),
				deck.getDescription(),
				deck.getCreatedAt(),
				deck.getUpdatedAt());
	}

}
