package com.engramdecks.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.engramdecks.api.model.Flashcard;

public interface FlashcardRepository extends MongoRepository<Flashcard, String> {

	List<Flashcard> findByDeckId(String deckId);

	void deleteByDeckId(String deckId);

}
