package org.cognitia.course_ms.domain.review.dto;

public record GetCourseReviewsRequest(
        Long courseId,
        int start,
        int end
) {
}
