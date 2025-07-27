package org.cognitia.course_ms.domain.lesson.dto;

public record LessonMetadataDto(
        String title,
        String description,
        Long path,
        String skill,
        Long courseId,
        String authorId
) {
}
