package org.cognitia.video_ms.infra.dto.video;

import org.springframework.web.multipart.MultipartFile;

public record UploadVideoRequest(
        VideoMetadataDto metadata,
        MultipartFile video
) {
}
