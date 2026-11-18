package com.engramdecks.api.dto.request;

import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DeckCoverRequestValidator implements ConstraintValidator<ValidDeckCover, DeckCoverRequest> {

	private static final Pattern COLOR = Pattern.compile("#[0-9A-Fa-f]{6}");

	private static final Pattern IMAGE = Pattern.compile(
			"data:image/(jpeg|png|webp);base64,[A-Za-z0-9+/]+={0,2}");

	@Override
	public boolean isValid(DeckCoverRequest request, ConstraintValidatorContext context) {
		if (request == null || request.type() == null) {
			return true;
		}
		if (request.value() == null || request.value().isBlank()) {
			return false;
		}
		String value = request.value().trim();
		return switch (request.type()) {
			case COLOR -> COLOR.matcher(value).matches();
			case IMAGE -> IMAGE.matcher(value).matches();
			default -> false;
		};
	}

}
