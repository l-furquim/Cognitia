package org.cognitia.video_ms.domain.exceptions;

public class InvalidVideoDeletionException extends RuntimeException {
    public InvalidVideoDeletionException(String message) {
        super(message);
    }
}
