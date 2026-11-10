package com.engramdecks.api.dto.request;

import jakarta.validation.constraints.Size;

public record PatchFlashcardRequest(
		@Size(max = 5000, message = "Front must be at most 5000 characters")
		String front,

		@Size(max = 5000, message = "Back must be at most 5000 characters")
		String back
) {
}
