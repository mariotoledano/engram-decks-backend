package com.engramdecks.api.mapper;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.request.PatchFlashcardRequest;
import com.engramdecks.api.dto.request.UpdateFlashcardRequest;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.model.Flashcard;

@Component
public class FlashcardMapper {

	public Flashcard toEntity(Set<String> deckIds, CreateFlashcardRequest request) {
		return Flashcard.builder()
				.deckIds(copyDeckIds(deckIds))
				.front(request.front())
				.back(request.back())
				.tags(TagNormalizer.normalize(request.tags()))
				.build();
	}

	public void update(Flashcard flashcard, UpdateFlashcardRequest request, Set<String> deckIds) {
		flashcard.setFront(request.front());
		flashcard.setBack(request.back());
		flashcard.setDeckIds(copyDeckIds(deckIds));
		flashcard.setTags(TagNormalizer.normalize(request.tags()));
	}

	public void patch(Flashcard flashcard, PatchFlashcardRequest request) {
		if (request.front() != null) {
			flashcard.setFront(request.front());
		}
		if (request.back() != null) {
			flashcard.setBack(request.back());
		}
		if (request.tags() != null) {
			flashcard.setTags(TagNormalizer.normalize(request.tags()));
		}
	}

	public FlashcardResponse toResponse(Flashcard flashcard) {
		Set<String> deckIds = flashcard.getDeckIds();
		return new FlashcardResponse(
				flashcard.getId(),
				deckIds == null ? List.of() : List.copyOf(deckIds),
				flashcard.getFront(),
				flashcard.getBack(),
				copyTags(flashcard.getTags()),
				flashcard.getCreatedAt(),
				flashcard.getUpdatedAt());
	}

	private List<String> copyTags(Set<String> tags) {
		if (tags == null || tags.isEmpty()) {
			return List.of();
		}
		return List.copyOf(new TreeSet<>(tags));
	}

	private Set<String> copyDeckIds(Set<String> deckIds) {
		return deckIds == null ? new LinkedHashSet<>() : new LinkedHashSet<>(deckIds);
	}

}
