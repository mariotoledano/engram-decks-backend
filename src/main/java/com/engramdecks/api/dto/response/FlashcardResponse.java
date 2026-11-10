package com.engramdecks.api.dto.response;

import java.time.Instant;

public record FlashcardResponse(
		String id,
		String deckId,
		String front,
		String back,
		Instant createdAt,
		Instant updatedAt
) {
}
