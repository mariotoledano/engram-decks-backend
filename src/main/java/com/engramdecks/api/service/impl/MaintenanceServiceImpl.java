package com.engramdecks.api.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.engramdecks.api.dto.request.TagMatch;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.dto.response.MaintenanceSummaryResponse;
import com.engramdecks.api.mapper.FlashcardMapper;
import com.engramdecks.api.mapper.TagNormalizer;
import com.engramdecks.api.repository.DeckRepository;
import com.engramdecks.api.repository.FlashcardRepository;
import com.engramdecks.api.service.DeckService;
import com.engramdecks.api.service.MaintenanceService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceServiceImpl implements MaintenanceService {

	static final String CONFIRMATION = "BORRAR";

	private final DeckRepository deckRepository;
	private final FlashcardRepository flashcardRepository;
	private final FlashcardSearch flashcardSearch;
	private final FlashcardMapper flashcardMapper;
	private final DeckService deckService;

	@Override
	public MaintenanceSummaryResponse summary() {
		return new MaintenanceSummaryResponse(
				deckRepository.count(),
				flashcardRepository.count(),
				flashcardRepository.countUnlinked(),
				flashcardSearch.distinctTags().size());
	}

	@Override
	public List<FlashcardResponse> findFlashcards(String query, String tag, String deckId) {
		Set<String> tags = tag == null || tag.isBlank() ? Set.of() : TagNormalizer.normalize(Set.of(tag));
		List<String> deckIds = deckId == null || deckId.isBlank() ? List.of() : List.of(deckId.trim());
		return flashcardSearch.find(tags, TagMatch.ANY, deckIds, query).stream()
				.map(flashcardMapper::toResponse)
				.toList();
	}

	@Override
	public void deleteAllDecks(String confirm) {
		requireConfirm(confirm);
		log.info("Deleting every deck");
		deckService.deleteAll();
	}

	@Override
	public void deleteAllFlashcards(String confirm) {
		requireConfirm(confirm);
		log.info("Deleting every flashcard");
		flashcardRepository.deleteAll();
	}

	@Override
	public void deleteUnlinked(String confirm) {
		requireConfirm(confirm);
		long deleted = flashcardRepository.deleteUnlinked();
		log.info("Deleted {} unlinked flashcards", deleted);
	}

	private void requireConfirm(String confirm) {
		if (!CONFIRMATION.equals(confirm)) {
			log.warn("Rejected wipe confirmation");
			throw new IllegalArgumentException("Confirmation must be BORRAR");
		}
	}

}
