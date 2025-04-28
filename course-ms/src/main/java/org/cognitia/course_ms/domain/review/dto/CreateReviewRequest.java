package org.cognitia.course_ms.domain.review.dto;

public record CreateReviewRequest(
        Double rate,
        String description,
        String authorId,
        Long courseId,
        String authorName,
        String authorProfileUrl
) {
}
