package org.cognitia.video_ms.infra.dto.video;

public record UpdateVideoMetadataRequest(
        Long videoId,
        String title,
        String description,
        String skill,
        Long path
) {
}
