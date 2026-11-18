package com.engramdecks.api.dto.request;

import com.engramdecks.api.model.DeckCoverType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidDeckCover
public record DeckCoverRequest(
		@NotNull(message = "Cover type must not be null")
		DeckCoverType type,

		@NotBlank(message = "Cover value must not be blank")
		@Size(max = 100_000, message = "Cover value must be at most 100000 characters")
		String value
) {
}
