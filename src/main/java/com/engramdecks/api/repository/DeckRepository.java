package com.engramdecks.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.engramdecks.api.model.Deck;

public interface DeckRepository extends MongoRepository<Deck, String> {
}
