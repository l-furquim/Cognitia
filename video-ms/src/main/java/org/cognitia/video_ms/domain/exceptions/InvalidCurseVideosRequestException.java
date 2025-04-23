package org.cognitia.video_ms.domain.exceptions;

public class InvalidCurseVideosRequestException extends RuntimeException {
    public InvalidCurseVideosRequestException(String message) {
        super(message);
    }
}
