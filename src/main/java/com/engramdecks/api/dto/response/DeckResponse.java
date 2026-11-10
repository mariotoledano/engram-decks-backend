package com.engramdecks.api.dto.response;

import java.time.Instant;

public record DeckResponse(
		String id,
		String name,
		String description,
		Instant createdAt,
		Instant updatedAt
) {
}
