package org.cognitia.video_ms.domain.model;

public record Video(
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
