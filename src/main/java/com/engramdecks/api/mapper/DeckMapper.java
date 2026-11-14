package com.engramdecks.api.mapper;

import org.springframework.stereotype.Component;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.PatchDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.model.Deck;

@Component
public class DeckMapper {

	public Deck toEntity(CreateDeckRequest request) {
		return Deck.builder()
				.name(request.name())
				.description(request.description())
				.tags(TagNormalizer.normalize(request.tags()))
				.build();
	}

	public void update(Deck deck, UpdateDeckRequest request) {
		deck.setName(request.name());
		deck.setDescription(request.description());
		deck.setTags(TagNormalizer.normalize(request.tags()));
	}

	public void patch(Deck deck, PatchDeckRequest request) {
		if (request.name() != null) {
			deck.setName(request.name());
		}
		if (request.description() != null) {
			deck.setDescription(request.description());
		}
		if (request.tags() != null) {
			deck.setTags(TagNormalizer.normalize(request.tags()));
		}
	}

	public DeckResponse toResponse(Deck deck) {
		return new DeckResponse(
				deck.getId(),
				deck.getName(),
				deck.getDescription(),
				deck.getTags(),
				deck.getCreatedAt(),
				deck.getUpdatedAt());
	}

}
