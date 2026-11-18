package com.engramdecks.api.dto.response;

import java.util.List;

public record StudySessionPreviewResponse(
		List<FlashcardResponse> cards,
		int totalMatched
) {
}
