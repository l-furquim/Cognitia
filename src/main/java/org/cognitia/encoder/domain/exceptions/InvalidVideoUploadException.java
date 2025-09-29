package org.cognitia.encoder.domain.exceptions;

public class InvalidVideoUploadException extends RuntimeException {
    public InvalidVideoUploadException(String message) {
        super(message);
    }
}
