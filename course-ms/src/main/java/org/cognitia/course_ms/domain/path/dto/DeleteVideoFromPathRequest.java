package org.cognitia.course_ms.domain.path.dto;

public record DeleteVideoFromPathRequest(
        String userId,
        Long pathId,
        Long videoId
) {
}
