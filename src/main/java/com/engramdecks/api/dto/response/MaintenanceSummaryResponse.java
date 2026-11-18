package com.engramdecks.api.dto.response;

public record MaintenanceSummaryResponse(
		long deckCount,
		long flashcardCount,
		long unlinkedCount,
		long tagCount
) {
}
