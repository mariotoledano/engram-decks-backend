package com.engramdecks.api.mapper;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public final class TagNormalizer {

	private TagNormalizer() {
	}

	public static Set<String> normalize(Set<String> tags) {
		if (tags == null || tags.isEmpty()) {
			return new HashSet<>();
		}
		Set<String> normalized = new HashSet<>();
		for (String tag : tags) {
			normalized.add(tag.trim().toLowerCase(Locale.ROOT));
		}
		return normalized;
	}

}
