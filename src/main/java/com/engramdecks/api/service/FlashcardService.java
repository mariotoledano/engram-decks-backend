package com.engramdecks.api.service;

import java.util.List;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.request.PatchFlashcardRequest;
import com.engramdecks.api.dto.request.UpdateFlashcardRequest;
import com.engramdecks.api.dto.response.FlashcardResponse;

public interface FlashcardService {

	List<FlashcardResponse> findByDeckId(String deckId);

	List<FlashcardResponse> findUnlinked(String query);

	FlashcardResponse findById(String id);

	FlashcardResponse create(String deckId, CreateFlashcardRequest request);

	FlashcardResponse create(CreateFlashcardRequest request);

	FlashcardResponse update(String id, UpdateFlashcardRequest request);

	FlashcardResponse patch(String id, PatchFlashcardRequest request);

	void unlinkFromDeck(String deckId, String flashcardId);

	void delete(String id);

}
