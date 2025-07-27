package org.cognitia.course_ms.domain.lesson.exceptions.dto;

public record GlobalExceptionResponse(
        String path,
        Integer status,
        String error,
        String timestamp
) {
}
