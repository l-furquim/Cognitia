package org.cognitia.encoder.domain.exceptions.dto;

public record GlobalExceptionResponse(
        String path,
        Integer status,
        String error,
        String timestamp
) {
}
