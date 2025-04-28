package org.cognitia.course_ms.domain.review.exceptions;

public class InvalidReviewDeletionRequest extends RuntimeException {
    public InvalidReviewDeletionRequest(String message) {
        super(message);
    }
}
