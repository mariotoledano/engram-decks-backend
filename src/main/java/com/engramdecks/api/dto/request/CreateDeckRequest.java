package com.engramdecks.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDeckRequest(
		@NotBlank(message = "Name must not be blank")
		@Size(max = 120, message = "Name must be at most 120 characters")
		String name,

		@Size(max = 2000, message = "Description must be at most 2000 characters")
		String description
) {
}
