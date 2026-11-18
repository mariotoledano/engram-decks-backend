package com.engramdecks.api.dto.request;

import java.util.List;
import java.util.Set;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PreviewStudySessionRequest(
		@Size(max = 20, message = "Tags must contain at most 20 items")
		Set<
				@NotBlank(message = "Tag must not be blank")
				@Size(max = 40, message = "Tag must be at most 40 characters")
				@Pattern(regexp = "^[\\p{L}\\p{N} _-]+$", message = "Tag contains invalid characters")
				String
		> tags,

		TagMatch match,

		@NotNull(message = "Limit is required")
		@Min(value = 1, message = "Limit must be at least 1")
		@Max(value = 200, message = "Limit must be at most 200")
		Integer limit,

		Boolean shuffle,

		@Size(max = 50, message = "Deck ids must contain at most 50 items")
		List<@NotBlank(message = "Deck id must not be blank") String> deckIds,

		@Size(max = 200, message = "Query must be at most 200 characters")
		String query
) {
}
