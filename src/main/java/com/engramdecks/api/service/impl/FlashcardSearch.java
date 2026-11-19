package com.engramdecks.api.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.engramdecks.api.dto.request.TagMatch;
import com.engramdecks.api.model.Flashcard;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FlashcardSearch {

	private final MongoTemplate mongoTemplate;

	public List<Flashcard> find(Set<String> tags, TagMatch match, List<String> deckIds, String query) {
		List<Criteria> criteria = new ArrayList<>();
		if (tags != null && !tags.isEmpty()) {
			if (match == TagMatch.ALL) {
				criteria.add(Criteria.where(Flashcard::getTags).all(tags));
			} else {
				criteria.add(Criteria.where(Flashcard::getTags).in(tags));
			}
		}
		if (deckIds != null && !deckIds.isEmpty()) {
			criteria.add(Criteria.where(Flashcard::getDeckIds).in(deckIds));
		}
		if (query != null && !query.isBlank()) {
			String pattern = RegexEscaper.escape(query.trim());
			criteria.add(new Criteria().orOperator(
					Criteria.where(Flashcard::getFront).regex(pattern, "i"),
					Criteria.where(Flashcard::getBack).regex(pattern, "i")));
		}

		Query mongoQuery = new Query();
		if (!criteria.isEmpty()) {
			mongoQuery.addCriteria(new Criteria().andOperator(criteria));
		}
		return mongoTemplate.find(mongoQuery, Flashcard.class);
	}

	public List<String> distinctTags() {
		return mongoTemplate.findDistinct(new Query(), "tags", Flashcard.class, String.class).stream()
				.filter(tag -> tag != null && !tag.isBlank())
				.map(tag -> tag.trim())
				.distinct()
				.sorted()
				.toList();
	}

}
