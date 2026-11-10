package com.engramdecks.api.dto.response;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
		Instant timestamp,
		int status,
		String message,
		List<String> details
) {

	public ErrorResponse {
		details = details == null ? List.of() : List.copyOf(details);
	}

}
