package com.engramdecks.api.mapper;

import org.springframework.stereotype.Component;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.DeckCoverRequest;
import com.engramdecks.api.dto.request.PatchDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.dto.response.DeckCoverResponse;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.model.Deck;
import com.engramdecks.api.model.DeckCover;

@Component
public class DeckMapper {

	public Deck toEntity(CreateDeckRequest request) {
		return Deck.builder()
				.name(request.name())
				.description(request.description())
				.tags(TagNormalizer.normalize(request.tags()))
				.cover(coverOf(request.cover()))
				.build();
	}

	public void update(Deck deck, UpdateDeckRequest request) {
		deck.setName(request.name());
		deck.setDescription(request.description());
		deck.setTags(TagNormalizer.normalize(request.tags()));
		if (request.cover() != null) {
			deck.setCover(coverOf(request.cover()));
		} else if (deck.getCover() == null) {
			deck.setCover(DeckCover.defaultColor());
		}
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
		if (request.cover() != null) {
			deck.setCover(coverOf(request.cover()));
		}
	}

	public DeckResponse toResponse(Deck deck, long cardCount) {
		DeckCover cover = deck.getCover() == null ? DeckCover.defaultColor() : deck.getCover();
		return new DeckResponse(
				deck.getId(),
				deck.getName(),
				deck.getDescription(),
				deck.getTags(),
				new DeckCoverResponse(cover.getType(), cover.getValue()),
				deck.getCreatedAt(),
				deck.getUpdatedAt(),
				deck.getLastTimeStudiedAt(),
				cardCount);
	}

	private static DeckCover coverOf(DeckCoverRequest request) {
		if (request == null) {
			return DeckCover.defaultColor();
		}
		return new DeckCover(request.type(), request.value().trim());
	}

}
