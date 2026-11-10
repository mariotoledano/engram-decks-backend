package com.engramdecks.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.engramdecks.api.dto.request.CreateFlashcardRequest;
import com.engramdecks.api.dto.request.PatchFlashcardRequest;
import com.engramdecks.api.dto.request.UpdateFlashcardRequest;
import com.engramdecks.api.dto.response.ErrorResponse;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.service.FlashcardService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Flashcards")
@RestController
@RequiredArgsConstructor
public class FlashcardController {

	private final FlashcardService flashcardService;

	@Operation(summary = "List flashcards in a deck")
	@ApiResponse(responseCode = "200", description = "Flashcards retrieved")
	@ApiResponse(responseCode = "404", description = "Deck not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("/api/v1/decks/{deckId}/flashcards")
	public ResponseEntity<List<FlashcardResponse>> findByDeckId(@PathVariable String deckId) {
		return ResponseEntity.ok(flashcardService.findByDeckId(deckId));
	}

	@Operation(summary = "Create a flashcard in a deck")
	@ApiResponse(responseCode = "201", description = "Flashcard created")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "Deck not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping("/api/v1/decks/{deckId}/flashcards")
	public ResponseEntity<FlashcardResponse> create(
			@PathVariable String deckId,
			@Valid @RequestBody CreateFlashcardRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(flashcardService.create(deckId, request));
	}

	@Operation(summary = "Get a flashcard by id")
	@ApiResponse(responseCode = "200", description = "Flashcard retrieved")
	@ApiResponse(responseCode = "404", description = "Flashcard not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("/api/v1/flashcards/{id}")
	public ResponseEntity<FlashcardResponse> findById(@PathVariable String id) {
		return ResponseEntity.ok(flashcardService.findById(id));
	}

	@Operation(summary = "Replace a flashcard")
	@ApiResponse(responseCode = "200", description = "Flashcard updated")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "Flashcard not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PutMapping("/api/v1/flashcards/{id}")
	public ResponseEntity<FlashcardResponse> update(
			@PathVariable String id,
			@Valid @RequestBody UpdateFlashcardRequest request) {
		return ResponseEntity.ok(flashcardService.update(id, request));
	}

	@Operation(summary = "Partially update a flashcard")
	@ApiResponse(responseCode = "200", description = "Flashcard updated")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "Flashcard not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PatchMapping("/api/v1/flashcards/{id}")
	public ResponseEntity<FlashcardResponse> patch(
			@PathVariable String id,
			@Valid @RequestBody PatchFlashcardRequest request) {
		return ResponseEntity.ok(flashcardService.patch(id, request));
	}

	@Operation(summary = "Delete a flashcard")
	@ApiResponse(responseCode = "204", description = "Flashcard deleted")
	@ApiResponse(responseCode = "404", description = "Flashcard not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@DeleteMapping("/api/v1/flashcards/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		flashcardService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
