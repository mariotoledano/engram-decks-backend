package com.engramdecks.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeckCover {

	/** Same orange as the home-screen buttons in the mobile light palette. */
	public static final String DEFAULT_COLOR = "#C4622D";

	private DeckCoverType type;

	private String value;

	public static DeckCover defaultColor() {
		return new DeckCover(DeckCoverType.COLOR, DEFAULT_COLOR);
	}

}
