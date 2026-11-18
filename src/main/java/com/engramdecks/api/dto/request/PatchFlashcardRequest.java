package com.engramdecks.api.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PatchFlashcardRequest(
		@Size(max = 5000, message = "Front must be at most 5000 characters")
		String front,

		@Size(max = 5000, message = "Back must be at most 5000 characters")
		String back,

		@Size(max = 20, message = "Tags must contain at most 20 items")
		Set<
				@NotBlank(message = "Tag must not be blank")
				@Size(max = 40, message = "Tag must be at most 40 characters")
				@Pattern(regexp = "^[\\p{L}\\p{N} _-]+$", message = "Tag contains invalid characters")
				String
		> tags
) {
}
