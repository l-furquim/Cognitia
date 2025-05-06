package org.cognitia.course_ms.domain.path.dto;

public record CreatePathRequest(
        String title,
        String userId,
        Long courseId
) {
}
