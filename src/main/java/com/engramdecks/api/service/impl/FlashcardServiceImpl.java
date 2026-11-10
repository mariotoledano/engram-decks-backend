package com.engramdecks.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.request.PatchFlashcardRequest;
import com.engramdecks.api.dto.request.UpdateFlashcardRequest;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.exception.ResourceNotFoundException;
import com.engramdecks.api.mapper.FlashcardMapper;
import com.engramdecks.api.model.Flashcard;
import com.engramdecks.api.repository.DeckRepository;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.FlashcardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

	private final FlashcardRepository flashcardRepository;
	private final DeckRepository deckRepository;
	private final FlashcardMapper flashcardMapper;

	@Override
	public List<FlashcardResponse> findByDeckId(String deckId) {
		requireDeck(deckId);
		return flashcardRepository.findByDeckId(deckId).stream()
				.map(flashcardMapper::toResponse)
				.toList();
	}

	@Override
	public FlashcardResponse findById(String id) {
		return flashcardMapper.toResponse(findFlashcard(id));
	}

	@Override
	public FlashcardResponse create(String deckId, CreateFlashcardRequest request) {
		requireDeck(deckId);
		log.info("Creating flashcard in deck {}", deckId);
		Flashcard flashcard = flashcardRepository.save(flashcardMapper.toEntity(deckId, request));
		return flashcardMapper.toResponse(flashcard);
	}

	@Override
	public FlashcardResponse update(String id, UpdateFlashcardRequest request) {
		Flashcard flashcard = findFlashcard(id);
		flashcardMapper.update(flashcard, request);
		return flashcardMapper.toResponse(flashcardRepository.save(flashcard));
	}

	@Override
	public FlashcardResponse patch(String id, PatchFlashcardRequest request) {
		validatePatch(request);
		Flashcard flashcard = findFlashcard(id);
		flashcardMapper.patch(flashcard, request);
		return flashcardMapper.toResponse(flashcardRepository.save(flashcard));
	}

	@Override
	public void delete(String id) {
		findFlashcard(id);
		log.info("Deleting flashcard {}", id);
		flashcardRepository.deleteById(id);
	}

	private Flashcard findFlashcard(String id) {
		return flashcardRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with id: " + id));
	}

	private void requireDeck(String deckId) {
		if (!deckRepository.existsById(deckId)) {
			throw new ResourceNotFoundException("Deck not found with id: " + deckId);
		}
	}

	private void validatePatch(PatchFlashcardRequest request) {
		if (request.front() != null && request.front().isBlank()) {
			throw new IllegalArgumentException("Front must not be blank");
		}
		if (request.back() != null && request.back().isBlank()) {
			throw new IllegalArgumentException("Back must not be blank");
		}
	}

}
