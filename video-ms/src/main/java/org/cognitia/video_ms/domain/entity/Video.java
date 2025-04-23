package org.cognitia.video_ms.domain.entity;

public record Video(
        String title,
        String description,
        Long path,
        String skill,
        Double duration,
        String thumbUrl,
        Long courseId
) {
}
