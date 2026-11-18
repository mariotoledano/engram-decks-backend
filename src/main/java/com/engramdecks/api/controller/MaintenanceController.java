package com.engramdecks.api.controller;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.engramdecks.api.dto.request.ConfirmWipeRequest;
import com.engramdecks.api.dto.response.ErrorResponse;
import com.engramdecks.api.dto.response.FlashcardResponse;
import com.engramdecks.api.dto.response.MaintenanceSummaryResponse;
import com.engramdecks.api.service.MaintenanceService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Maintenance")
@RestController
@RequestMapping("/api/v1/maintenance")
@RequiredArgsConstructor
@ConditionalOnProperty(name = "engram.maintenance.enabled", havingValue = "true")
public class MaintenanceController {

	private final MaintenanceService maintenanceService;

	@Operation(summary = "Count decks, flashcards, unlinked flashcards, and tags")
	@ApiResponse(responseCode = "200", description = "Summary retrieved")
	@GetMapping("/summary")
	public ResponseEntity<MaintenanceSummaryResponse> summary() {
		return ResponseEntity.ok(maintenanceService.summary());
	}

	@Operation(summary = "List flashcards, optionally filtered")
	@ApiResponse(responseCode = "200", description = "Flashcards retrieved")
	@GetMapping("/flashcards")
	public ResponseEntity<List<FlashcardResponse>> findFlashcards(
			@RequestParam(name = "q", required = false) String query,
			@RequestParam(name = "tag", required = false) String tag,
			@RequestParam(name = "deckId", required = false) String deckId) {
		return ResponseEntity.ok(maintenanceService.findFlashcards(query, tag, deckId));
	}

	@Operation(summary = "Delete every deck and unlink its flashcards")
	@ApiResponse(responseCode = "204", description = "Decks deleted")
	@ApiResponse(responseCode = "400", description = "Confirmation missing",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@DeleteMapping("/decks")
	public ResponseEntity<Void> deleteAllDecks(@Valid @RequestBody ConfirmWipeRequest request) {
		maintenanceService.deleteAllDecks(request.confirm());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Delete every flashcard")
	@ApiResponse(responseCode = "204", description = "Flashcards deleted")
	@ApiResponse(responseCode = "400", description = "Confirmation missing",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@DeleteMapping("/flashcards")
	public ResponseEntity<Void> deleteAllFlashcards(@Valid @RequestBody ConfirmWipeRequest request) {
		maintenanceService.deleteAllFlashcards(request.confirm());
		return ResponseEntity.noContent().build();
	}

	@Operation(summary = "Delete flashcards that belong to no deck")
	@ApiResponse(responseCode = "204", description = "Unlinked flashcards deleted")
	@ApiResponse(responseCode = "400", description = "Confirmation missing",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@DeleteMapping("/unlinked")
	public ResponseEntity<Void> deleteUnlinked(@Valid @RequestBody ConfirmWipeRequest request) {
		maintenanceService.deleteUnlinked(request.confirm());
		return ResponseEntity.noContent().build();
	}

}
