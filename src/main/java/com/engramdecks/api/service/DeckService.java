package com.engramdecks.api.service;

import java.util.List;

import com.engramdecks.api.dto.request.CreateDeckRequest;
import com.engramdecks.api.dto.request.PatchDeckRequest;
import com.engramdecks.api.dto.request.UpdateDeckRequest;
import com.engramdecks.api.dto.response.DeckResponse;

public interface DeckService {

	List<DeckResponse> findAll();

	DeckResponse findById(String id);

	DeckResponse create(CreateDeckRequest request);

	DeckResponse update(String id, UpdateDeckRequest request);

	DeckResponse patch(String id, PatchDeckRequest request);

	void delete(String id);

}
