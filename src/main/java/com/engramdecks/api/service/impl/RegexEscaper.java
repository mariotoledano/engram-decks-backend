package com.engramdecks.api.service.impl;

/**
 * Mongo text search uses a regular expression, so user input must be escaped.
 */
public final class RegexEscaper {

	private static final String REGEX_METACHARACTERS = "\\.^$|?*+()[]{}";

	private RegexEscaper() {
	}

	public static String escape(String value) {
		StringBuilder escaped = new StringBuilder(value.length());
		for (int index = 0; index < value.length(); index++) {
			char character = value.charAt(index);
			if (REGEX_METACHARACTERS.indexOf(character) >= 0) {
				escaped.append('\\');
			}
			escaped.append(character);
		}
		return escaped.toString();
	}

}
