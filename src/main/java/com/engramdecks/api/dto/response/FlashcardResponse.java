package com.engramdecks.api.dto.response;

import java.time.Instant;
import java.util.List;

public record FlashcardResponse(
		String id,
		List<String> deckIds,
		String front,
		String back,
		List<String> tags,
		Instant createdAt,
		Instant updatedAt
) {
}
