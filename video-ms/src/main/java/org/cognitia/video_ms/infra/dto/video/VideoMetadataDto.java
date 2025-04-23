package org.cognitia.video_ms.infra.dto.video;

public record VideoMetadataDto(
        String title,
        String description,
        Long path,
        String skill,
        Long courseId
) {
}
