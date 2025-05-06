package org.cognitia.course_ms.domain.path.exceptions;

public class InvalidCreatePathRequest extends RuntimeException {
    public InvalidCreatePathRequest(String message) {
        super(message);
    }
}
