package com.engramdecks.api.dto.request;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PatchDeckRequest(
		@Size(max = 120, message = "Name must be at most 120 characters")
		String name,

		@Size(max = 2000, message = "Description must be at most 2000 characters")
		String description,

		@Size(max = 20, message = "Tags must contain at most 20 items")
		Set<
				@NotBlank(message = "Tag must not be blank")
				@Size(max = 40, message = "Tag must be at most 40 characters")
				@Pattern(regexp = "^[\\p{L}\\p{N} _-]+$", message = "Tag contains invalid characters")
				String
		> tags
) {
}
