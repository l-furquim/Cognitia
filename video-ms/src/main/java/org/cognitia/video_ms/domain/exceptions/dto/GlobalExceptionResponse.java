package org.cognitia.video_ms.domain.exceptions.dto;

public record GlobalExceptionResponse(
        String path,
        Integer status,
        String error,
        String timestamp
) {
}
