package org.cognitia.course_ms.domain.path.dto;

public record VideoDto(
        String title,
        String description,
        String originalName,
        Long path,
        String skill,
        Double duration,
        String chunksUrl,
        String thumbUrl,
        Long courseId
) {
}
