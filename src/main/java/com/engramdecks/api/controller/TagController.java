package com.engramdecks.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engramdecks.api.service.TagService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Tags")
@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

	private final TagService tagService;

	@Operation(summary = "List distinct flashcard tags")
	@ApiResponse(responseCode = "200", description = "Tags retrieved")
	@GetMapping
	public ResponseEntity<List<String>> findAll() {
		return ResponseEntity.ok(tagService.findAll());
	}

}
