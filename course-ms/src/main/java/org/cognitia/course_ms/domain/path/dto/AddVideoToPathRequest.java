package org.cognitia.course_ms.domain.path.dto;

public record AddVideoToPathRequest(
        String userId,
        Long pathId,
        Long videoId
) {
}
