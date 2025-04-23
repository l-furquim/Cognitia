package org.cognitia.video_ms.domain.exceptions;

public class InvalidVideoUploadException extends RuntimeException {
    public InvalidVideoUploadException(String message) {
        super(message);
    }
}
