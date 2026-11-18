package com.engramdecks.api.service.impl;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
		return flashcardRepository.findByDeckIdsContaining(deckId).stream()
				.map(flashcardMapper::toResponse)
				.toList();
	}

	@Override
	public List<FlashcardResponse> findUnlinked(String query) {
		String trimmed = query == null ? "" : query.trim();
		List<Flashcard> flashcards = trimmed.isEmpty()
				? flashcardRepository.findUnlinked()
				: flashcardRepository.findUnlinkedMatching(RegexEscaper.escape(trimmed));
		return flashcards.stream()
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
		Flashcard flashcard = flashcardRepository.save(flashcardMapper.toEntity(Set.of(deckId), request));
		return flashcardMapper.toResponse(flashcard);
	}

	@Override
	public FlashcardResponse create(CreateFlashcardRequest request) {
		Set<String> deckIds = requireDecks(request.deckIds());
		log.info("Creating flashcard in {} decks", deckIds.size());
		Flashcard flashcard = flashcardRepository.save(flashcardMapper.toEntity(deckIds, request));
		return flashcardMapper.toResponse(flashcard);
	}

	@Override
	public FlashcardResponse update(String id, UpdateFlashcardRequest request) {
		Flashcard flashcard = findFlashcard(id);
		Set<String> deckIds = requireDecks(request.deckIds());
		flashcardMapper.update(flashcard, request, deckIds);
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
	public void unlinkFromDeck(String deckId, String flashcardId) {
		Flashcard flashcard = findFlashcard(flashcardId);
		Set<String> deckIds = flashcard.getDeckIds();
		if (deckIds != null && deckIds.remove(deckId)) {
			log.info("Unlinking flashcard {} from deck {}", flashcardId, deckId);
			flashcardRepository.save(flashcard);
		}
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

	private Set<String> requireDecks(List<String> deckIds) {
		if (deckIds == null || deckIds.isEmpty()) {
			return new LinkedHashSet<>();
		}
		Set<String> unique = new LinkedHashSet<>();
		for (String deckId : deckIds) {
			if (deckId == null || deckId.isBlank()) {
				throw new IllegalArgumentException("Deck id must not be blank");
			}
			unique.add(deckId);
		}
		for (String deckId : unique) {
			if (!deckRepository.existsById(deckId)) {
				throw new IllegalArgumentException("Unknown deck id: " + deckId);
			}
		}
		return unique;
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
