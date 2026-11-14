package com.engramdecks.api.dto.response;

import java.time.Instant;
import java.util.Set;

public record DeckResponse(
		String id,
		String name,
		String description,
		Set<String> tags,
		Instant createdAt,
		Instant updatedAt
) {
}
