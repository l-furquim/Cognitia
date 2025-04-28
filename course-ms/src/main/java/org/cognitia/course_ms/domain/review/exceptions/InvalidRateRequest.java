package org.cognitia.course_ms.domain.review.exceptions;

public class InvalidRateRequest extends RuntimeException {
    public InvalidRateRequest(String message) {
        super(message);
    }
}
