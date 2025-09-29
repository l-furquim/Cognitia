package org.cognitia.encoder.infra.dto.video;

public record VideoQualityConfig(
        String name,
        String resolution,
        String videoBitrate,
        String audioBitrate
) {}
