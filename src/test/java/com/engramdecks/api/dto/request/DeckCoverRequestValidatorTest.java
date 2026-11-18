package com.engramdecks.api.dto.request;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.engramdecks.api.model.DeckCoverType;

class DeckCoverRequestValidatorTest {

	private final DeckCoverRequestValidator validator = new DeckCoverRequestValidator();

	@Test
	void acceptsAHexColor() {
		assertTrue(validator.isValid(new DeckCoverRequest(DeckCoverType.COLOR, "#C4622D"), null));
	}

	@Test
	void rejectsAColorThatIsNotHex() {
		assertFalse(validator.isValid(new DeckCoverRequest(DeckCoverType.COLOR, "orange"), null));
	}

	@Test
	void acceptsASmallPngDataUrl() {
		assertTrue(validator.isValid(
				new DeckCoverRequest(DeckCoverType.IMAGE, "data:image/png;base64,aaaa"),
				null));
	}

	@Test
	void rejectsAnImageThatIsNotADataUrl() {
		assertFalse(validator.isValid(
				new DeckCoverRequest(DeckCoverType.IMAGE, "https://example.com/cover.png"),
				null));
	}

	@Test
	void rejectsBlankValueEvenWhenTypeIsSet() {
		assertFalse(validator.isValid(new DeckCoverRequest(DeckCoverType.COLOR, "   "), null));
		assertFalse(validator.isValid(new DeckCoverRequest(DeckCoverType.IMAGE, ""), null));
	}

}
