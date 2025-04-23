package org.cognitia.video_ms.infra.dto.video;

public record UploadVideoThumbMetadataDto(
        Long videoId,
        String userId
) {
}
