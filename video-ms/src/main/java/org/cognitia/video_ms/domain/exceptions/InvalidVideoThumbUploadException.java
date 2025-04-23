package org.cognitia.video_ms.domain.exceptions;

public class InvalidVideoThumbUploadException extends RuntimeException {
    public InvalidVideoThumbUploadException(String message) {
        super(message);
    }
}
