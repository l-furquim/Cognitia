package org.cognitia.video_ms.infra.dto.video;

import org.cognitia.video_ms.domain.model.Video;

public record UploadVideoResponse(
        Video video
) {
}
