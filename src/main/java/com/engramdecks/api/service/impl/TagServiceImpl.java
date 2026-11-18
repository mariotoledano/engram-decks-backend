package com.engramdecks.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.engramdecks.api.service.TagService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

	private final FlashcardSearch flashcardSearch;

	@Override
	public List<String> findAll() {
		return flashcardSearch.distinctTags();
	}

}
