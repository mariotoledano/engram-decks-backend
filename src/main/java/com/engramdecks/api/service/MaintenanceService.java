package com.engramdecks.api.service;

import java.util.List;

import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.dto.response.MaintenanceSummaryResponse;

public interface MaintenanceService {

	MaintenanceSummaryResponse summary();

	List<FlashcardResponse> findFlashcards(String query, String tag, String deckId);

	void deleteAllDecks(String confirm);

	void deleteAllFlashcards(String confirm);

	void deleteUnlinked(String confirm);

}
