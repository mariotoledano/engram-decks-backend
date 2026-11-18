package com.engramdecks.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ConfirmWipeRequest(
		@NotBlank(message = "Confirmation is required")
		String confirm
) {
}
