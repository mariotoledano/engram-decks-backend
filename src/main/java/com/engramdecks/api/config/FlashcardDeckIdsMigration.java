package com.engramdecks.api.config;

import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FlashcardDeckIdsMigration implements ApplicationRunner {

	private static final String COLLECTION = "flashcards";

	private final MongoTemplate mongoTemplate;

	@Override
	public void run(ApplicationArguments args) {
		Query legacy = Query.query(Criteria.where("deckId").exists(true));
		var documents = mongoTemplate.find(legacy, Document.class, COLLECTION);
		for (Document document : documents) {
			Object rawDeckId = document.get("deckId");
			Update update = new Update().unset("deckId");
			if (rawDeckId != null && !rawDeckId.toString().isBlank()) {
				update.addToSet("deckIds", rawDeckId.toString());
			}
			mongoTemplate.updateFirst(
					Query.query(Criteria.where("_id").is(document.get("_id"))),
					update,
					COLLECTION);
		}
		if (!documents.isEmpty()) {
			log.info("Migrated {} flashcards from deckId to deckIds", documents.size());
		}
	}

}
