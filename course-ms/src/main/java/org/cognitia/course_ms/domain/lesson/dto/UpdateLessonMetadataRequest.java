package org.cognitia.course_ms.domain.lesson.dto;

public record UpdateLessonMetadataRequest(
        Long videoId,
        String title,
        String description,
        String skill,
        Long path
) {
}
