package org.cognitia.course_ms.domain.lesson;

public record Lesson(
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
