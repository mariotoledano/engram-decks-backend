package com.engramdecks.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.dto.response.ErrorResponse;
import com.engramdecks.api.service.DeckService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Decks")
@RestController
@RequestMapping("/api/v1/decks")
@RequiredArgsConstructor
public class DeckController {

	private final DeckService deckService;

	@Operation(summary = "List all decks")
	@ApiResponse(responseCode = "200", description = "Decks retrieved")
	@GetMapping
	public ResponseEntity<List<DeckResponse>> findAll() {
		return ResponseEntity.ok(deckService.findAll());
	}

	@Operation(summary = "Get a deck by id")
	@ApiResponse(responseCode = "200", description = "Deck retrieved")
	@ApiResponse(responseCode = "404", description = "Deck not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@GetMapping("/{id}")
	public ResponseEntity<DeckResponse> findById(@PathVariable String id) {
		return ResponseEntity.ok(deckService.findById(id));
	}

	@Operation(summary = "Create a deck")
	@ApiResponse(responseCode = "201", description = "Deck created")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PostMapping
	public ResponseEntity<DeckResponse> create(@Valid @RequestBody CreateDeckRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(deckService.create(request));
	}

	@Operation(summary = "Replace a deck")
	@ApiResponse(responseCode = "200", description = "Deck updated")
	@ApiResponse(responseCode = "400", description = "Invalid request",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@ApiResponse(responseCode = "404", description = "Deck not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@PutMapping("/{id}")
	public ResponseEntity<DeckResponse> update(@PathVariable String id, @Valid @RequestBody UpdateDeckRequest request) {
		return ResponseEntity.ok(deckService.update(id, request));
	}

	@Operation(summary = "Delete a deck and its flashcards")
	@ApiResponse(responseCode = "204", description = "Deck deleted")
	@ApiResponse(responseCode = "404", description = "Deck not found",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		deckService.delete(id);
		return ResponseEntity.noContent().build();
	}

}
