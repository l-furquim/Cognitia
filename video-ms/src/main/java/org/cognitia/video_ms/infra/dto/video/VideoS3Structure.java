package org.cognitia.video_ms.infra.dto.video;

import java.util.List;
import java.util.Map;

public record VideoS3Structure(
        String masterManifestUrl,
        String thumbnailUrl,
        Map<String, List<String>> qualityFiles,
        List<String> rootFiles
) {}
