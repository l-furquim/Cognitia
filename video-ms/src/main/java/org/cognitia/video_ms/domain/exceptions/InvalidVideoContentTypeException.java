package org.cognitia.video_ms.domain.exceptions;

public class InvalidVideoContentTypeException extends RuntimeException {
    public InvalidVideoContentTypeException(String message) {
        super(message);
    }
}
