package com.engramdecks.api.config;

import org.bson.Document;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.engramdecks.api.model.DeckCover;
import com.engramdecks.api.model.DeckCoverType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeckCoverMigration implements ApplicationRunner {

	private static final String COLLECTION = "decks";

	private final MongoTemplate mongoTemplate;

	@Override
	public void run(ApplicationArguments args) {
		Query missing = Query.query(new Criteria().orOperator(
				Criteria.where("cover").exists(false),
				Criteria.where("cover").is(null)));
		Document cover = new Document("type", DeckCoverType.COLOR.name())
				.append("value", DeckCover.DEFAULT_COLOR);
		long updated = mongoTemplate.updateMulti(missing, new Update().set("cover", cover), COLLECTION)
				.getModifiedCount();
		if (updated > 0) {
			log.info("Set the default cover color on {} decks", updated);
		}
	}

}
