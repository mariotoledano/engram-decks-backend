package com.engramdecks.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFlashcardRequest(
		@NotBlank(message = "Front must not be blank")
		@Size(max = 5000, message = "Front must be at most 5000 characters")
		String front,

		@NotBlank(message = "Back must not be blank")
		@Size(max = 5000, message = "Back must be at most 5000 characters")
		String back
) {
}
