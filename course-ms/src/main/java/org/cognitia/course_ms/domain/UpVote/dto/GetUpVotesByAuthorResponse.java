package org.cognitia.course_ms.domain.UpVote.dto;

import org.cognitia.course_ms.domain.model.UpVote;

import java.util.List;

public record GetUpVotesByAuthorResponse(
        List<UpVote> upvotes
) {
}
