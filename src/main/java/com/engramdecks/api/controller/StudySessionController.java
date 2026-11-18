package com.engramdecks.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engramdecks.api.dto.request.PreviewStudySessionRequest;
import com.engramdecks.api.dto.request.SaveStudySessionRequest;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.dto.response.ErrorResponse;
import com.engramdecks.api.dto.response.StudySessionPreviewResponse;
import com.engramdecks.api.service.StudySessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Study sessions")
@RestController
@RequestMapping("/api/v1/study-sessions")
@RequiredArgsConstructor
public class StudySessionController {

	private final StudySessionService studySessionService;

	@Operation(summary = "Preview a study session without saving a deck")
	@ApiResponse(responseCode = "200", description = "Session preview")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping("/preview")
	public ResponseEntity<StudySessionPreviewResponse> preview(
			@Valid @RequestBody PreviewStudySessionRequest request) {
		return ResponseEntity.ok(studySessionService.preview(request));
	}

	@Operation(summary = "Save a previewed session as a deck that links the chosen flashcards")
	@ApiResponse(responseCode = "201", description = "Deck created")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "Flashcard not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping
	public ResponseEntity<DeckResponse> save(@Valid @RequestBody SaveStudySessionRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(studySessionService.save(request));
	}

}
