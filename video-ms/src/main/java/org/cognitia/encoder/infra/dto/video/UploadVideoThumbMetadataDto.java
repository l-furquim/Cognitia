package org.cognitia.encoder.infra.dto.video;

public record UploadVideoThumbMetadataDto(
        Long videoId,
        String userId
) {
}
