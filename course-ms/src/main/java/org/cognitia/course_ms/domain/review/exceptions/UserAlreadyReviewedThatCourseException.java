package org.cognitia.course_ms.domain.review.exceptions;

public class UserAlreadyReviewedThatCourseException extends RuntimeException {
    public UserAlreadyReviewedThatCourseException(String message) {
        super(message);
    }
}
