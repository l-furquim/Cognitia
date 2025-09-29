package org.cognitia.encoder.infra.dto.video;

import org.cognitia.encoder.domain.model.Video;

public record UploadVideoResponse(
        Video video
) {
}
