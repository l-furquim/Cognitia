package org.cognitia.course_ms.domain.review.exceptions;

public class RateCourseNotFoundException extends RuntimeException {
    public RateCourseNotFoundException(String message) {
        super(message);
    }
}
