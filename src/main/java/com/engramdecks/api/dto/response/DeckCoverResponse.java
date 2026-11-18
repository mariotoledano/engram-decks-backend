package com.engramdecks.api.dto.response;

import com.engramdecks.api.model.DeckCoverType;

public record DeckCoverResponse(
		DeckCoverType type,
		String value
) {
}
