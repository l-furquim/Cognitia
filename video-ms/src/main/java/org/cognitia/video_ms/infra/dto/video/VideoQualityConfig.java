package org.cognitia.video_ms.infra.dto.video;

public record VideoQualityConfig(
        String name,
        String resolution,
        String videoBitrate,
        String audioBitrate
) {}
