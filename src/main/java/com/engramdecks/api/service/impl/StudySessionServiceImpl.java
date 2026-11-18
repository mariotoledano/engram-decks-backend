package com.engramdecks.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.PreviewStudySessionRequest;
import com.engramdecks.api.dto.request.SaveStudySessionRequest;
import com.engramdecks.api.dto.request.TagMatch;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.dto.response.StudySessionPreviewResponse;
import com.engramdecks.api.exception.ResourceNotFoundException;
import com.engramdecks.api.mapper.FlashcardMapper;
import com.engramdecks.api.mapper.TagNormalizer;
import com.engramdecks.api.model.Flashcard;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.DeckService;
import com.engramdecks.api.service.StudySessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudySessionServiceImpl implements StudySessionService {

	private final FlashcardSearch flashcardSearch;
	private final FlashcardRepository flashcardRepository;
	private final FlashcardMapper flashcardMapper;
	private final DeckService deckService;

	@Override
	public StudySessionPreviewResponse preview(PreviewStudySessionRequest request) {
		List<Flashcard> matched = new ArrayList<>(flashcardSearch.find(
				TagNormalizer.normalize(request.tags()),
				request.match() == null ? TagMatch.ANY : request.match(),
				cleanIds(request.deckIds()),
				request.query()));
		int totalMatched = matched.size();
		if (Boolean.TRUE.equals(request.shuffle())) {
			Collections.shuffle(matched);
		}
		int limit = Math.min(request.limit(), matched.size());
		List<Flashcard> chosen = matched.subList(0, limit);
		log.info("Previewed study session: {} of {} flashcards", chosen.size(), totalMatched);
		return new StudySessionPreviewResponse(
				chosen.stream().map(flashcardMapper::toResponse).toList(),
				totalMatched);
	}

	@Override
	public DeckResponse save(SaveStudySessionRequest request) {
		List<String> ids = cleanIds(request.flashcardIds());
		if (ids.isEmpty()) {
			throw new IllegalArgumentException("At least one flashcard is required");
		}

		List<Flashcard> flashcards = new ArrayList<>();
		for (String id : ids) {
			flashcards.add(flashcardRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Flashcard not found with id: " + id)));
		}

		DeckResponse deck = deckService.create(new CreateDeckRequest(
				request.name().trim(),
				request.description(),
				request.tags(),
				null));

		for (Flashcard flashcard : flashcards) {
			Set<String> deckIds = flashcard.getDeckIds();
			if (deckIds == null) {
				deckIds = new LinkedHashSet<>();
				flashcard.setDeckIds(deckIds);
			}
			deckIds.add(deck.id());
			flashcardRepository.save(flashcard);
		}

		log.info("Saved study session as deck {} with {} flashcards", deck.id(), flashcards.size());
		return deck;
	}

	private static List<String> cleanIds(List<String> ids) {
		if (ids == null || ids.isEmpty()) {
			return List.of();
		}
		Set<String> unique = new LinkedHashSet<>();
		for (String id : ids) {
			if (id == null || id.isBlank()) {
				throw new IllegalArgumentException("Id must not be blank");
			}
			unique.add(id);
		}
		return List.copyOf(unique);
	}

}
