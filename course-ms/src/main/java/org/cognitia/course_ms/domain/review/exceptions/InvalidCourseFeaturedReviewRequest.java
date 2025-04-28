package org.cognitia.course_ms.domain.review.exceptions;

public class InvalidCourseFeaturedReviewRequest extends RuntimeException {
    public InvalidCourseFeaturedReviewRequest(String message) {
        super(message);
    }
}
