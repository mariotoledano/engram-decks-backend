package com.engramdecks.api.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;

class TagNormalizerTest {

	@Test
	void nullAndEmptyBecomeAnEmptySet() {
		assertEquals(Set.of(), TagNormalizer.normalize(null));
		assertEquals(Set.of(), TagNormalizer.normalize(Set.of()));
	}

	@Test
	void trimsAndLowercases() {
		assertEquals(Set.of("java"), TagNormalizer.normalize(Set.of(" Java ", "JAVA")));
	}

}
