package org.cognitia.encoder.infra.dto.video;

import org.springframework.web.multipart.MultipartFile;

public record UploadVideoThumbRequest(
        UploadVideoThumbMetadataDto metadata,
        MultipartFile image
) {
}
