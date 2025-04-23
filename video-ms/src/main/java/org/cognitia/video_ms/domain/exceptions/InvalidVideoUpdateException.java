package org.cognitia.video_ms.domain.exceptions;

public class InvalidVideoUpdateException extends RuntimeException {
    public InvalidVideoUpdateException(String message) {
        super(message);
    }
}
