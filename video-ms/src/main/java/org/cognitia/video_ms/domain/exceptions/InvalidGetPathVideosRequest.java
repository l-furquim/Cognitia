package org.cognitia.video_ms.domain.exceptions;

public class InvalidGetPathVideosRequest extends RuntimeException {
    public InvalidGetPathVideosRequest(String message) {
        super(message);
    }
}
