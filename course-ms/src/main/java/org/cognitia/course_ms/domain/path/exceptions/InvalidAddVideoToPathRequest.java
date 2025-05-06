package org.cognitia.course_ms.domain.path.exceptions;

public class InvalidAddVideoToPathRequest extends RuntimeException {
    public InvalidAddVideoToPathRequest(String message) {
        super(message);
    }
}
