package org.cognitia.course_ms.domain.review.exceptions;

public class AuthorNotRegistredInCourseRateException extends RuntimeException {
    public AuthorNotRegistredInCourseRateException(String message) {
        super(message);
    }
}
