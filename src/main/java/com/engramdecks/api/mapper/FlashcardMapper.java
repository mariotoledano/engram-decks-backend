package com.engramdecks.api.mapper;

import org.springframework.stereotype.Component;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.request.PatchFlashcardRequest;
import com.engramdecks.api.dto.request.UpdateFlashcardRequest;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.model.Flashcard;

@Component
public class FlashcardMapper {

	public Flashcard toEntity(String deckId, CreateFlashcardRequest request) {
		return Flashcard.builder()
				.deckId(deckId)
				.front(request.front())
				.back(request.back())
				.build();
	}

	public void update(Flashcard flashcard, UpdateFlashcardRequest request) {
		flashcard.setFront(request.front());
		flashcard.setBack(request.back());
	}

	public void patch(Flashcard flashcard, PatchFlashcardRequest request) {
		if (request.front() != null) {
			flashcard.setFront(request.front());
		}
		if (request.back() != null) {
			flashcard.setBack(request.back());
		}
	}

	public FlashcardResponse toResponse(Flashcard flashcard) {
		return new FlashcardResponse(
				flashcard.getId(),
				flashcard.getDeckId(),
				flashcard.getFront(),
				flashcard.getBack(),
				flashcard.getCreatedAt(),
				flashcard.getUpdatedAt());
	}

}
