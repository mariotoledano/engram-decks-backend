package com.engramdecks.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import com.engramdecks.api.model.Flashcard;

public interface FlashcardRepository extends MongoRepository<Flashcard, String> {

	String UNLINKED = "{ $or: [ { deckIds: { $exists: false } }, { deckIds: null }, { deckIds: { $size: 0 } } ] }";

	List<Flashcard> findByDeckIdsContaining(String deckId);

	@Query("{ 'deckIds': ?0 }")
	@Update("{ '$pull': { 'deckIds': ?0 } }")
	void pullDeckId(String deckId);

	@Query(UNLINKED)
	List<Flashcard> findUnlinked();

	@Query(value = UNLINKED, count = true)
	long countUnlinked();

	@Query(value = UNLINKED, delete = true)
	long deleteUnlinked();

	@Query("{ $and: [ "
			+ UNLINKED + ", "
			+ "{ $or: [ { front: { $regex: ?0, $options: 'i' } }, { back: { $regex: ?0, $options: 'i' } } ] } "
			+ "] }")
	List<Flashcard> findUnlinkedMatching(String pattern);

}
