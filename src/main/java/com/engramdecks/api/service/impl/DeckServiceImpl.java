package com.engramdecks.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.PatchDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.exception.ResourceNotFoundException;
import com.engramdecks.api.mapper.DeckMapper;
import com.engramdecks.api.model.Deck;
import com.engramdecks.api.repository.DeckRepository;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.DeckService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService {

	private final DeckRepository deckRepository;
	private final FlashcardRepository flashcardRepository;
	private final DeckMapper deckMapper;

	@Override
	public List<DeckResponse> findAll() {
		return deckRepository.findAll().stream()
				.map(deckMapper::toResponse)
				.toList();
	}

	@Override
	public DeckResponse findById(String id) {
		return deckMapper.toResponse(findDeck(id));
	}

	@Override
	public DeckResponse create(CreateDeckRequest request) {
		log.info("Creating deck");
		Deck deck = deckRepository.save(deckMapper.toEntity(request));
		return deckMapper.toResponse(deck);
	}

	@Override
	public DeckResponse update(String id, UpdateDeckRequest request) {
		Deck deck = findDeck(id);
		deckMapper.update(deck, request);
		return deckMapper.toResponse(deckRepository.save(deck));
	}

	@Override
	public DeckResponse patch(String id, PatchDeckRequest request) {
		validatePatch(request);
		Deck deck = findDeck(id);
		deckMapper.patch(deck, request);
		return deckMapper.toResponse(deckRepository.save(deck));
	}

	@Override
	public void delete(String id) {
		findDeck(id);
		log.info("Deleting deck {} and its flashcards", id);
		flashcardRepository.deleteByDeckId(id);
		deckRepository.deleteById(id);
	}

	private Deck findDeck(String id) {
		return deckRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Deck not found with id: " + id));
	}

	private void validatePatch(PatchDeckRequest request) {
		if (request.name() != null && request.name().isBlank()) {
			throw new IllegalArgumentException("Name must not be blank");
		}
	}

}
