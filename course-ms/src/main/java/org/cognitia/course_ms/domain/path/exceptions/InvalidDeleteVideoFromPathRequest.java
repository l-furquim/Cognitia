package org.cognitia.course_ms.domain.path.exceptions;

public class InvalidDeleteVideoFromPathRequest extends RuntimeException {
    public InvalidDeleteVideoFromPathRequest(String message) {
        super(message);
    }
}
