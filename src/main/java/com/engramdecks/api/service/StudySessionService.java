package com.engramdecks.api.service;

import com.engramdecks.api.dto.request.PreviewStudySessionRequest;
import com.engramdecks.api.dto.request.SaveStudySessionRequest;
import com.engramdecks.api.dto.response.DeckResponse;
import com.engramdecks.api.dto.response.StudySessionPreviewResponse;

public interface StudySessionService {

	StudySessionPreviewResponse preview(PreviewStudySessionRequest request);

	DeckResponse save(SaveStudySessionRequest request);

}
